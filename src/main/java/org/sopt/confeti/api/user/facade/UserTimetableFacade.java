package org.sopt.confeti.api.user.facade;

import lombok.AllArgsConstructor;
import org.sopt.confeti.annotation.Facade;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableDTO;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.timetablefestival.TimetableFestival;
import org.sopt.confeti.domain.timetablefestival.application.TimetableFestivalService;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Facade
@AllArgsConstructor
public class UserTimetableFacade {
    UserService userService;
    TimetableFestivalService timetableFestivalService;
    private final S3FileHandler s3FileHandler;

    @Transactional(readOnly = true)
    public UserTimetableDTO getTimetablesListAndDate(long userId) {
        userService.existsById(userId);
        List<TimetableFestival> festivalList =  timetableFestivalService.getFetivalList(userId);
        return UserTimetableDTO.of(festivalList, s3FileHandler);
    }
}

