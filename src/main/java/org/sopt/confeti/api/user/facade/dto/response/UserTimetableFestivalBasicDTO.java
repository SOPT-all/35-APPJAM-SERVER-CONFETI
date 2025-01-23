package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.festivaldate.FestivalDate;
import org.sopt.confeti.domain.usertimetable.UserTimetable;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public record UserTimetableFestivalBasicDTO(LocalTime ticketOpenAt, List<UserTimetableFestivalStageDTO> stages) {
    public static UserTimetableFestivalBasicDTO of(FestivalDate festivalDate, Map<Long, UserTimetable> userTimetables  )  {
        return new UserTimetableFestivalBasicDTO(
                festivalDate.getOpenAt(),
                festivalDate.getStages()
                        .stream()
                        .map(stages -> UserTimetableFestivalStageDTO.of(stages, userTimetables))
                        .toList()
        );
    }
}
