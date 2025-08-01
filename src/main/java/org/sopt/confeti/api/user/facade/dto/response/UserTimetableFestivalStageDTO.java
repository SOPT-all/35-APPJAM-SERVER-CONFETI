package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;
import java.util.Map;
import org.sopt.confeti.domain.festival_stage.FestivalStage;
import org.sopt.confeti.domain.user_timetable.UserTimetable_DEPRECATED;

public record UserTimetableFestivalStageDTO(
        int stageOrder,
        String stageName,
        List<UserTimetableFestivalTimeDTO> festivalTimes
) {
    public static UserTimetableFestivalStageDTO of(FestivalStage festivalStage, Map<Long, UserTimetable_DEPRECATED> userTimetables
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
