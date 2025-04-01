package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableFestivalBasicDTO;
import org.sopt.confeti.global.util.DateConvertor;

public record UserTimetableFestivalResponse(
        String ticketOpenAt,
        int stageCount,
        List<UserTimetableFestivalStageResponse> stages
) {
    public static UserTimetableFestivalResponse from(UserTimetableFestivalBasicDTO timetableFestival) {
        return new UserTimetableFestivalResponse(
                DateConvertor.convertToDefaultFormat(timetableFestival.festivalDate(),
                        timetableFestival.ticketOpenAt()),
                timetableFestival.stages().size(),
                timetableFestival.stages().stream()
                        .map(stage -> UserTimetableFestivalStageResponse.of(timetableFestival.festivalDate(), stage))
                        .toList()
        );
    }
}