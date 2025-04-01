package org.sopt.confeti.api.user.dto.response;

import java.time.LocalDate;
import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableFestivalStageDTO;

public record UserTimetableFestivalStageResponse(
        int stageOrder,
        String stageName,
        List<UserTimetableFestivalTimeResponse> festivalTimes
) {
    public static UserTimetableFestivalStageResponse of(LocalDate festivalDate,
                                                        UserTimetableFestivalStageDTO festivalStage) {
        return new UserTimetableFestivalStageResponse(
                festivalStage.stageOrder(),
                festivalStage.stageName(),
                festivalStage.festivalTimes().stream()
                        .map(festivalTime -> UserTimetableFestivalTimeResponse.of(festivalDate, festivalTime))
                        .toList()
        );
    }
}