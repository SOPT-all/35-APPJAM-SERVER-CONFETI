package org.sopt.confeti.api.user.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.user_timetable.UserTimetable_DEPRECATED;

public record UserTimetableFestivalBasicDTO(
        LocalDate festivalDate,
        LocalTime ticketOpenAt,
        List<UserTimetableFestivalStageDTO> stages
) {
    public static UserTimetableFestivalBasicDTO of(FestivalDate festivalDate, Map<Long, UserTimetable_DEPRECATED> userTimetables) {
        return new UserTimetableFestivalBasicDTO(
                festivalDate.getFestivalAt(),
                festivalDate.getOpenAt(),
                festivalDate.getStages()
                        .stream()
                        .map(stages -> UserTimetableFestivalStageDTO.of(stages, userTimetables))
                        .toList()
        );
    }
}
