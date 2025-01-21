package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserTimetableFestivalBasicDTO;
import org.sopt.confeti.global.util.DateConvertor;

import java.util.List;

public record UserTimetableFestivalResponse (
        String ticketOpenAt,
        Integer stageCount,
        List<UserTimetableFestivalStageResponse> stages
){
    public static UserTimetableFestivalResponse from(UserTimetableFestivalBasicDTO timetableFestival) {
        return new UserTimetableFestivalResponse(
                DateConvertor.convert(timetableFestival.ticketOpenAt()),
                timetableFestival.stages().size(),
                timetableFestival.stages().stream()
                        .map(UserTimetableFestivalStageResponse::from)
                        .toList()
        );
    }
}