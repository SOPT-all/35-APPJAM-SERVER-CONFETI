package org.sopt.confeti.api.user.facade;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.facade.dto.request.AddTimetableFestivalArtiestDTO;
import org.sopt.confeti.api.user.facade.dto.request.AddTimetableFestivalDTO;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimetableDTO;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimetableFestivalDTO;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimetableListDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableDatesDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableToAddDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableDetailFestivalsDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableEntireFestivalDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableFestivalBasicDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableHistoryDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetablesDTO;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.festival.application.dto.FestivalCursorDTO;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.festival_date.application.FestivalDateService;
import org.sopt.confeti.domain.festival_time.FestivalTime;
import org.sopt.confeti.domain.timetable_festival.TimetableFestival;
import org.sopt.confeti.domain.timetable_festival.TimetableFestivalCursor.CursorData;
import org.sopt.confeti.domain.timetable_festival.application.TimetableFestivalService;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.domain.user_timetable.UserTimetable;
import org.sopt.confeti.domain.user_timetable.application.UserTimetableService;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.sopt.confeti.global.common.CursorPage;
import org.sopt.confeti.global.common.constant.PerformanceStatus;
import org.sopt.confeti.global.common.constant.TimetableSortType;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.exception.ConflictException;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.interceptor.auth.UserContext;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class UserTimetableFacade {

    private static final int NEXT_CURSOR_SIZE = 1;
    private static final int TIMETABLE_FESTIVAL_COUNT_MAXIMUM = 5;
    private static final int TIMETABLE_FESTIVALS_TO_ADD_SIZE = 6 + NEXT_CURSOR_SIZE;

    private final UserService userService;
    private final TimetableFestivalService timetableFestivalService;
    private final FestivalService festivalService;
    private final FestivalDateService festivalDateService;
    private final UserTimetableService userTimetableService;

    @ReadOnlyTransactional
    public UserTimetableDetailFestivalsDTO getTimetablesListAndDate() {
        List<TimetableFestival> festivalList = timetableFestivalService.getFetivalList(
            UserContext.get().id());
        return UserTimetableDetailFestivalsDTO.from(festivalList);
    }

    @Transactional
    public void removeTimetableFestival(long festivalId) {
        long userId = UserContext.get().id();

        validateExistFestival(festivalId);
        validateExistTimetableFestival(userId, festivalId);

        timetableFestivalService.removeTimetableFestival(userId, festivalId);
    }

    @Transactional
    public void addTimetableFestivals(AddTimetableFestivalDTO from) {
        long userId = UserContext.get().id();

        User user = userService.findUserTimetablesById(userId);
        List<Festival> addFestivals = festivalService.findFestivalsByIdIn(
            from.festivals().stream()
                .distinct()
                .map(AddTimetableFestivalArtiestDTO::festivalId)
                .toList()
        );

        validateDuplicateTimetableFestival(
            user.getTimetableFestivals().stream()
                .map(TimetableFestival::getFestival)
                .toList(),
            addFestivals
        );
        validateCountTimetableFestival(user.getTimetableFestivals().size(), addFestivals.size());

        timetableFestivalService.addTimetableFestivals(user, addFestivals);
        userService.updateHasTimetableHistory(userId);
    }

    @Transactional
    protected void validateDuplicateTimetableFestival(final List<Festival> currentFestivals,
        final List<Festival> addFestivals) {
        if (
            currentFestivals.stream()
                .anyMatch(currentFestival -> addFestivals.stream()
                    .anyMatch(Predicate.isEqual(currentFestival)))
        ) {
            throw new ConflictException(ErrorMessage.CONFLICT);
        }
    }

    @ReadOnlyTransactional
    protected void validateCountTimetableFestival(final long currentCount, final int addCount) {
        if (currentCount + addCount > TIMETABLE_FESTIVAL_COUNT_MAXIMUM) {
            throw new ConflictException(ErrorMessage.TIMETABLE_FESTIVAL_IS_FULL);
        }
    }

    @ReadOnlyTransactional
    protected void validateExistFestival(final long festivalId) {
        if (!festivalService.existsById(festivalId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @ReadOnlyTransactional
    protected void validateExistTimetableFestival(final long userId, final long festivalId) {
        if (!timetableFestivalService.existsByUserIdAndFestivalId(userId, festivalId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @ReadOnlyTransactional
    public CursorPage<TimetableToAddDTO> getTimetablesToAdd(Long cursor) {
        long userId = UserContext.get().id();

        if (cursor == null) {
            List<Festival> festivals = festivalService.findFestivalsUsingInitCursor(
                UserContext.get().id(),
                TIMETABLE_FESTIVALS_TO_ADD_SIZE);
            return CursorPage.of(
                festivals.stream()
                    .map(TimetableToAddDTO::from)
                    .toList(),
                TIMETABLE_FESTIVALS_TO_ADD_SIZE
            );
        }

        // 커서 값 조회
        FestivalCursorDTO festivalCursorDTO = getFestivalCursor(userId, cursor);

        List<Festival> festivals = festivalService.findFestivalsUsingCursor(userId,
            festivalCursorDTO.cursorTitle(),
            festivalCursorDTO.cursorIsFavorite(), TIMETABLE_FESTIVALS_TO_ADD_SIZE);
        return CursorPage.of(
            festivals.stream()
                .map(TimetableToAddDTO::from)
                .toList(),
            TIMETABLE_FESTIVALS_TO_ADD_SIZE
        );
    }

    public UserTimetableFestivalBasicDTO getTimetableInfo(long festivalDateId) {
        FestivalDate festivalDate = festivalDateService.findFestivalDateId(festivalDateId);
        return getUserTimetableDTO(UserContext.get().id(), festivalDate);
    }

    @ReadOnlyTransactional
    protected void validateUserExists(final long userId) {
        if (!userService.existsById(userId)) {
            throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED);
        }
    }

    @ReadOnlyTransactional
    public FestivalCursorDTO getFestivalCursor(final long userId, final long cursor) {
        return festivalService.findFestivalCursor(userId, cursor)
            .orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND)
            );
    }

    @Transactional
    public void patchTimetableFestivals(PatchTimetableDTO timetableDTO) {
        List<UserTimetable> userTimetables = userTimetableService.getUserTimeTables(
            UserContext.get().id());
        validateExistUserTimetables(userTimetables);
        validateExistTimetableIds(userTimetables, timetableDTO);

        userTimetableService.patchTimetableFestival(userTimetables, timetableDTO);
    }

    @Deprecated
    @ReadOnlyTransactional
    public UserTimetablesDTO getTimetables_deprecated(final long userId, final String sortBy) {
        validateUserExists(userId);
        validateSortType(sortBy);

        List<TimetableFestival> userTimetables = timetableFestivalService.getTimetables(userId,
            sortBy);

        return UserTimetablesDTO.from(userTimetables);
    }

    @ReadOnlyTransactional
    public CursorPage<TimetableFestival> getTimetableCursorPage(TimetableSortType sortBy,
        CursorData cursor, PerformanceStatus performanceStatus) {
        return sortBy.getTimetableCursorPage(timetableFestivalService, UserContext.get().id(),
            cursor,
            performanceStatus);
    }

    @ReadOnlyTransactional
    public UserTimetablesDTO getTimetablesPreview() {
        List<TimetableFestival> userTimetables = timetableFestivalService.getTimetablesPreview(
            UserContext.get().id());
        return UserTimetablesDTO.from(userTimetables);
    }

    protected void validateExistFestivalTimeIds(final List<Long> festivalTimeIds) {
        if (festivalTimeIds == null || festivalTimeIds.isEmpty()) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    private UserTimetableFestivalBasicDTO getUserTimetableDTO(final long userId,
        FestivalDate festivalDate) {
        List<Long> festivalTimeIds = festivalDate.getStages().stream()
            .flatMap(festivalStage -> festivalStage.getTimes().stream())
            .map(FestivalTime::getId)
            .toList();
        validateExistFestivalTimeIds(festivalTimeIds);

        List<UserTimetable> userTimetables = userTimetableService.getUserTimetablesByFestivalTimeId(
            userId, festivalTimeIds);
        validateExistUserTimetables(userTimetables);

        Map<Long, UserTimetable> userTimetableMapper = createUserTimetableMapper(userTimetables);
        validateExistUserTimetableMapper(userTimetableMapper);

        return UserTimetableFestivalBasicDTO.of(festivalDate, userTimetableMapper);
    }

    private Map<Long, UserTimetable> createUserTimetableMapper(List<UserTimetable> userTimetables) {
        return userTimetables.stream()
            .collect(Collectors.toMap(userTimetable ->
                userTimetable.getFestivalTime().getId(), Function.identity())
            );
    }

    protected void validateExistUserTimetableMapper(Map<Long, UserTimetable> userTimetables) {
        if (userTimetables.isEmpty()) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    protected void validateExistUserTimetables(List<UserTimetable> userTimetables) {
        if (userTimetables.isEmpty()) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @ReadOnlyTransactional
    protected void validateExistTimetableIds(List<UserTimetable> userTimetables,
        PatchTimetableDTO timetableDTO) {
        Set<Long> existingIds = userTimetables.stream()
            .map(UserTimetable::getId)
            .collect(Collectors.toSet());

        for (PatchTimetableListDTO timetableListDTO : timetableDTO.userTimetables()) {
            if (!existingIds.contains(timetableListDTO.userTimetableId())) {
                throw new NotFoundException(ErrorMessage.NOT_FOUND);
            }
        }
    }

    @ReadOnlyTransactional
    protected void validateSortType(final String sortBy) {
        if (!sortBy.equalsIgnoreCase("createdAt") && !sortBy.equalsIgnoreCase("oldestFirst")) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }
    }

    @ReadOnlyTransactional
    public UserTimetableHistoryDTO getHasTimetableHistory() {
        boolean hasTimetableHistory = userService.getHasTimetableHistory(UserContext.get().id());
        return UserTimetableHistoryDTO.from(hasTimetableHistory);
    }

    public UserTimetableEntireFestivalDTO getEntireFestivalInfo(long festivalId) {
        TimetableFestival festival = timetableFestivalService.getEntireFestivalInfo(
            UserContext.get().id(),
            festivalId);
        return UserTimetableEntireFestivalDTO.from(festival);
    }

    public UserTimetableFestivalBasicDTO getEntireFestivalDateInfo(long festivalDateId) {
        FestivalDate festivalDate = festivalDateService.findEntireFestivalDateById(festivalDateId);
        return getUserTimetableDTO(UserContext.get().id(), festivalDate);
    }

    @Transactional
    public void updateTimetableFestivals(
        final PatchTimetableFestivalDTO patchTimetableFestivalDTO
    ) {
        timetableFestivalService.removeTimetableFestivals(
            UserContext.get().id(), patchTimetableFestivalDTO.deleteTimetableIds());
    }

    @ReadOnlyTransactional
    public TimetableDatesDTO getTimetableDates(Long timetableFestivalId) {
        TimetableFestival timetableFestival = timetableFestivalService.getWithFestival(
            timetableFestivalId);
        timetableFestival.validateOwner(UserContext.get().id());

        Festival festival = timetableFestival.getFestival();
        List<FestivalDate> festivalDates = festivalDateService.findAllByFestivalId(
            festival.getId());
        return TimetableDatesDTO.of(timetableFestival, festival, festivalDates);
    }

}

