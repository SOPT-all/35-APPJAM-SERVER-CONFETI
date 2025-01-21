package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.festivalstage.FestivalStage;

import java.util.List;

public record UserTimetableFestivalStageDTO (
        int stageOrder,
        String stageName,
        List<UserTimetableFestivalTimeDTO> festivalTimes
){
    public static UserTimetableFestivalStageDTO from(FestivalStage festivalStage
    ) {
        return new UserTimetableFestivalStageDTO(
                festivalStage.getOrder(),
                festivalStage.getName(),
                festivalStage.getTimes()
                        .stream()
                        .map(UserTimetableFestivalTimeDTO::from)
                        .toList()
        );
    }
}
