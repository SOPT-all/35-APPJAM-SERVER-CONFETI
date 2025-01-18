package org.sopt.confeti.api.user.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.annotation.Facade;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableDTO;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.timetablefestival.TimetableFestival;
import org.sopt.confeti.domain.timetablefestival.application.TimetableFestivalService;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Facade
@RequiredArgsConstructor
public class UserTimetableFacade {

    private final UserService userService;
    private final TimetableFestivalService timetableFestivalService;
    private final FestivalService festivalService;

    @Transactional(readOnly = true)
    public UserTimetableDTO getTimetablesListAndDate(long userId) {
        if (!userService.existsById(userId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        List<TimetableFestival> festivalList =  timetableFestivalService.getFetivalList(userId);
        return UserTimetableDTO.from(festivalList);
    }

    @Transactional
    public void removeTimetableFestival(final long userId, final long festivalId) {
        if (
                !userService.existsById(userId) ||
                        !festivalService.existsById(festivalId) ||
                        !timetableFestivalService.existsByUserIdAndFestivalId(userId, festivalId)
        ) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        timetableFestivalService.removeTimetableFestival(userId, festivalId);
    }
}

