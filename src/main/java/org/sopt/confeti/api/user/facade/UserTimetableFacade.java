package org.sopt.confeti.api.user.facade;

import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.annotation.Facade;
import org.sopt.confeti.api.user.facade.dto.request.AddTimetableFestivalArtiestDTO;
import org.sopt.confeti.api.user.facade.dto.request.AddTimetableFestivalDTO;
import org.sopt.confeti.api.user.facade.dto.response.TimetableToAddDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableDTO;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.festival.application.dto.FestivalCursorDTO;
import org.sopt.confeti.domain.timetablefestival.TimetableFestival;
import org.sopt.confeti.domain.timetablefestival.application.TimetableFestivalService;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.global.common.CursorPage;
import org.sopt.confeti.global.exception.ConflictException;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Facade
@RequiredArgsConstructor
public class UserTimetableFacade {

    private static final int TIMETABLE_FESTIVAL_COUNT_MAXIMUM = 3;
    private static final int NEXT_CURSOR_SIZE = 1;
    private static final int TIMETABLE_FESTIVALS_TO_ADD_SIZE = 6 + NEXT_CURSOR_SIZE;

    private final UserService userService;
    private final TimetableFestivalService timetableFestivalService;
    private final FestivalService festivalService;

    @Transactional(readOnly = true)
    public UserTimetableDTO getTimetablesListAndDate(long userId) {
        validateExistUser(userId);

        List<TimetableFestival> festivalList =  timetableFestivalService.getFetivalList(userId);
        return UserTimetableDTO.from(festivalList);
    }

    @Transactional
    public void removeTimetableFestival(final long userId, final long festivalId) {
        validateExistUser(userId);
        validateExistFestival(festivalId);
        validateExistTimetableFestival(userId, festivalId);

        timetableFestivalService.removeTimetableFestival(userId, festivalId);
    }

    @Transactional
    public void addTimetableFestivals(final long userId, final AddTimetableFestivalDTO from) {
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
    }

    @Transactional
    protected void validateDuplicateTimetableFestival(final List<Festival> currentFestivals, final List<Festival> addFestivals) {
        if (
                currentFestivals.stream()
                        .anyMatch(currentFestival -> addFestivals.stream()
                                .anyMatch(Predicate.isEqual(currentFestival)))
        ) {
            throw new ConflictException(ErrorMessage.CONFLICT);
        }
    }

    @Transactional(readOnly = true)
    protected void validateExistUser(final long userId) {
        if (!userService.existsById(userId)) {
            throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED);
        }
    }

    @Transactional(readOnly = true)
    protected void validateExistFestival(final long festivalId) {
        if (!festivalService.existsById(festivalId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    protected void validateCountTimetableFestival(final long currentCount, final int addCount) {
        if (currentCount + addCount > TIMETABLE_FESTIVAL_COUNT_MAXIMUM) {
            throw new ConflictException(ErrorMessage.TIMETABLE_FESTIVAL_IS_FULL);
        }
    }

    @Transactional(readOnly = true)
    protected void validateExistTimetableFestival(final long userId, final long festivalId) {
        if (!timetableFestivalService.existsByUserIdAndFestivalId(userId, festivalId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    public CursorPage<TimetableToAddDTO> getTimetablesToAdd(final long userId, final Long cursor) {
        if (cursor == null) {
            List<Festival> festivals = festivalService.findFestivalsUsingInitCursor(userId, TIMETABLE_FESTIVALS_TO_ADD_SIZE);
            return CursorPage.of(
                    festivals.stream()
                            .map(TimetableToAddDTO::from)
                            .toList(),
                    TIMETABLE_FESTIVALS_TO_ADD_SIZE
            );
        }

        // 커서 값 조회
        FestivalCursorDTO festivalCursorDTO = festivalService.findFestivalCursor(userId, cursor)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.NOT_FOUND)
                );

        List<Festival> festivals = festivalService.findFestivalsUsingCursor(userId, festivalCursorDTO.cursorTitle(), festivalCursorDTO.cursorIsFavorite(), TIMETABLE_FESTIVALS_TO_ADD_SIZE);
        return CursorPage.of(
                festivals.stream()
                        .map(TimetableToAddDTO::from)
                        .toList(),
                TIMETABLE_FESTIVALS_TO_ADD_SIZE
        );
    }
}

