package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserTimetableFestivalStageDTO;

import java.util.List;

public record UserTimetableFestivalStageResponse(
        int stageOrder,
        String stageName,
        List<UserTimetableFestivalTimeResponse> festivalTimes
){
    public static UserTimetableFestivalStageResponse from(UserTimetableFestivalStageDTO festivalStage) {
        return new UserTimetableFestivalStageResponse(
                festivalStage.stageOrder(),
                festivalStage.stageName(),
                festivalStage.festivalTimes().stream()
                        .map(UserTimetableFestivalTimeResponse::from)
                        .toList()
        );
    }
}