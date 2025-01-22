package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.festivaldate.FestivalDate;

import java.time.LocalTime;
import java.util.List;

public record UserTimetableFestivalBasicDTO(LocalTime ticketOpenAt, List<UserTimetableFestivalStageDTO> stages) {
    public static UserTimetableFestivalBasicDTO from(FestivalDate festivalDate) {
        return new UserTimetableFestivalBasicDTO(
                festivalDate.getOpenAt(),
                festivalDate.getStages()
                        .stream()
                        .map(UserTimetableFestivalStageDTO::from)
                        .toList()
        );
    }
}
