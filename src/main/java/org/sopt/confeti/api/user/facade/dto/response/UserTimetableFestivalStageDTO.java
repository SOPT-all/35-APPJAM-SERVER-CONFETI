package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.festival_stage.FestivalStage;
import org.sopt.confeti.domain.user_timetable.UserTimetable;

import java.util.List;
import java.util.Map;

public record UserTimetableFestivalStageDTO (
        int stageOrder,
        String stageName,
        List<UserTimetableFestivalTimeDTO> festivalTimes
){
    public static UserTimetableFestivalStageDTO of(FestivalStage festivalStage, Map<Long, UserTimetable> userTimetables
    ) {
        return new UserTimetableFestivalStageDTO(
                festivalStage.getOrder(),
                festivalStage.getName(),
                festivalStage.getTimes()
                        .stream()
                        .map(time -> UserTimetableFestivalTimeDTO.of(time, userTimetables))
                        .toList()
        );
    }
}
