package org.sopt.confeti.api.user.facade;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.facade.dto.request.AddTimetableArtistDTO;
import org.sopt.confeti.api.user.facade.dto.request.AddTimetableDTO;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimeBlockDTO;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimeBlockListDTO;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimetableDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableDatesDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableDetailFestivalsDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableEntireFestivalDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableFestivalBasicDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableHistoryDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableToAddDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetablesDTO;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.festival.application.dto.FestivalCursorDTO;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.festival_date.application.FestivalDateService;
import org.sopt.confeti.domain.festival_time.FestivalTime;
import org.sopt.confeti.domain.time_block.TimeBlock;
import org.sopt.confeti.domain.time_block.application.TimeBlockService;
import org.sopt.confeti.domain.timetable.Timetable;
import org.sopt.confeti.domain.timetable.TimetableCursor.CursorData;
import org.sopt.confeti.domain.timetable.application.TimetableService;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.application.UserService;
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
    private static final int TIMETABLE_COUNT_MAXIMUM = 5;
    private static final int TIMETABLES_TO_ADD_SIZE = 6 + NEXT_CURSOR_SIZE;

    private final UserService userService;
    private final TimetableService timetableService;
    private final FestivalService festivalService;
    private final FestivalDateService festivalDateService;
    private final TimeBlockService timeBlockService;

    @Transactional(readOnly = true)
    public TimetableDetailFestivalsDTO getTimetablesListAndDate() {
        List<Timetable> timetableList = timetableService.getFestivalList(
            UserContext.get().id());
        return TimetableDetailFestivalsDTO.from(timetableList);
    }

    @Transactional
    public void removeTimetable(long festivalId) {
        long userId = UserContext.get().id();

        validateExistFestival(festivalId);
        validateExistTimetable(userId, festivalId);

        timetableService.removeTimetable(userId, festivalId);
    }

    @Transactional
    public void addTimetables(AddTimetableDTO from) {
        long userId = UserContext.get().id();

        User user = userService.findUserTimetablesById(userId);
        List<Festival> addFestivals = festivalService.findFestivalsByIdIn(
            from.festivals().stream()
                .distinct()
                .map(AddTimetableArtistDTO::festivalId)
                .toList()
        );

        validateDuplicateTimetable(
            user.getTimetables().stream()
                .map(Timetable::getFestival)
                .toList(),
            addFestivals
        );
        validateCountTimetable(user.getTimetables().size(), addFestivals.size());

        timetableService.addTimetables(user, addFestivals);
        userService.updateHasTimetableHistory(userId);
    }

    @Transactional
    protected void validateDuplicateTimetable(final List<Festival> currentFestivals,
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
    protected void validateCountTimetable(final long currentCount, final int addCount) {
        if (currentCount + addCount > TIMETABLE_COUNT_MAXIMUM) {
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
    protected void validateExistTimetable(final long userId, final long festivalId) {
        if (!timetableService.existsByUserIdAndFestivalId(userId, festivalId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @ReadOnlyTransactional
    public CursorPage<TimetableToAddDTO> getTimetablesToAdd(Long cursor) {
        long userId = UserContext.get().id();

        if (cursor == null) {
            List<Festival> festivals = festivalService.findFestivalsUsingInitCursor(
                UserContext.get().id(),
                TIMETABLES_TO_ADD_SIZE);
            return CursorPage.of(
                festivals.stream()
                    .map(TimetableToAddDTO::from)
                    .toList(),
                TIMETABLES_TO_ADD_SIZE
            );
        }

        // 커서 값 조회
        FestivalCursorDTO festivalCursorDTO = getFestivalCursor(userId, cursor);

        List<Festival> festivals = festivalService.findFestivalsUsingCursor(userId,
            festivalCursorDTO.cursorTitle(),
            festivalCursorDTO.cursorIsFavorite(), TIMETABLES_TO_ADD_SIZE);
        return CursorPage.of(
            festivals.stream()
                .map(TimetableToAddDTO::from)
                .toList(),
            TIMETABLES_TO_ADD_SIZE
        );
    }

    @Transactional(readOnly = true)
    public TimetableFestivalBasicDTO getTimetableInfo(long festivalDateId) {
        FestivalDate festivalDate = festivalDateService.findFestivalDateId(festivalDateId);
        return getTimeBlockDTO(UserContext.get().id(), festivalDate);
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
    public void patchTimeBlocks(PatchTimeBlockDTO timeBlockDTO) {
        List<TimeBlock> timeBlocks = timeBlockService.getTimeBlocks(
            UserContext.get().id());
        validateExistTimeBlocks(timeBlocks);
        validateExistTimeBlockIds(timeBlocks, timeBlockDTO);

        timeBlockService.patchTimeBlocks(timeBlocks, timeBlockDTO);
    }

    @Deprecated
    @ReadOnlyTransactional
    public TimetablesDTO getTimetables_deprecated(final long userId, final String sortBy) {
        validateUserExists(userId);
        validateSortType(sortBy);

        List<Timetable> timetables = timetableService.getTimetables(userId,
            sortBy);

        return TimetablesDTO.from(timetables);
    }

    @ReadOnlyTransactional
    public CursorPage<Timetable> getTimetableCursorPage(TimetableSortType sortBy,
        CursorData cursor, PerformanceStatus performanceStatus) {
        return sortBy.getTimetableCursorPage(timetableService, UserContext.get().id(),
            cursor,
            performanceStatus);
    }

    @Transactional(readOnly = true)
    public TimetablesDTO getTimetablesPreview() {
        List<Timetable> timetables = timetableService.getTimetablesPreview(
            UserContext.get().id());
        return TimetablesDTO.from(timetables);
    }

    protected void validateExistFestivalTimeIds(final List<Long> festivalTimeIds) {
        if (festivalTimeIds == null || festivalTimeIds.isEmpty()) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    private TimetableFestivalBasicDTO getTimeBlockDTO(final long userId,
        FestivalDate festivalDate) {
        List<Long> festivalTimeIds = festivalDate.getStages().stream()
            .flatMap(festivalStage -> festivalStage.getTimes().stream())
            .map(FestivalTime::getId)
            .toList();
        validateExistFestivalTimeIds(festivalTimeIds);

        List<TimeBlock> timeBlocks = timeBlockService.getTimeBlocksByFestivalTimeId(
            userId, festivalTimeIds);
        validateExistTimeBlocks(timeBlocks);

        Map<Long, TimeBlock> timeBlockMapper = createTimeBlockMapper(timeBlocks);
        validateExistTimeBlockMapper(timeBlockMapper);

        return TimetableFestivalBasicDTO.of(festivalDate, timeBlockMapper);
    }

    private Map<Long, TimeBlock> createTimeBlockMapper(List<TimeBlock> timeBlocks) {
        return timeBlocks.stream()
            .collect(Collectors.toMap(timeBlock ->
                timeBlock.getFestivalTime().getId(), Function.identity())
            );
    }

    protected void validateExistTimeBlockMapper(Map<Long, TimeBlock> timeBlocks) {
        if (timeBlocks.isEmpty()) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    protected void validateExistTimeBlocks(List<TimeBlock> timeBlocks) {
        if (timeBlocks.isEmpty()) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @ReadOnlyTransactional
    protected void validateExistTimeBlockIds(List<TimeBlock> timeBlocks,
        PatchTimeBlockDTO timeBlockDTO) {
        Set<Long> existingIds = timeBlocks.stream()
            .map(TimeBlock::getId)
            .collect(Collectors.toSet());

        for (PatchTimeBlockListDTO timeBlockListDTO : timeBlockDTO.timeBlocks()) {
            if (!existingIds.contains(timeBlockListDTO.timeBlockId())) {
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
    public TimetableHistoryDTO getHasTimetableHistory() {
        boolean hasTimetableHistory = userService.getHasTimetableHistory(UserContext.get().id());
        return TimetableHistoryDTO.from(hasTimetableHistory);
    }

    public TimetableEntireFestivalDTO getEntireFestivalInfo(long festivalId) {
        Timetable timetable = timetableService.getEntireFestivalInfo(
            UserContext.get().id(),
            festivalId);
        return TimetableEntireFestivalDTO.from(timetable);
    }

    public TimetableFestivalBasicDTO getEntireFestivalDateInfo(long festivalDateId) {
        FestivalDate festivalDate = festivalDateService.findEntireFestivalDateById(festivalDateId);
        return getTimeBlockDTO(UserContext.get().id(), festivalDate);
    }

    @Transactional
    public void updateTimetables(
        final PatchTimetableDTO patchTimetableDTO
    ) {
        timetableService.removeTimetables(
            UserContext.get().id(), patchTimetableDTO.deleteTimetableIds());
    }

    @Transactional(readOnly = true)
    public TimetableDatesDTO getTimetableDates(Long timetableId) {
        Timetable timetable = timetableService.getWithFestival(
            timetableId);
        timetable.validateOwner(UserContext.get().id());

        Festival festival = timetable.getFestival();
        List<FestivalDate> festivalDates = festivalDateService.findAllByFestivalId(
            festival.getId());
        return TimetableDatesDTO.of(timetable, festival, festivalDates);
    }

}
