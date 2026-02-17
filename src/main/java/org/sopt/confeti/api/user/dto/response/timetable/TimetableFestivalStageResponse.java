package org.sopt.confeti.api.user.dto.response.timetable;

import java.time.LocalDate;
import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableFestivalStageDTO;

public record TimetableFestivalStageResponse(
    int stageOrder,
    String stageName,
    List<TimetableFestivalTimeResponse> festivalTimes
) {

    public static TimetableFestivalStageResponse of(LocalDate festivalDate,
        TimetableFestivalStageDTO festivalStage) {
        return new TimetableFestivalStageResponse(
            festivalStage.stageOrder(),
            festivalStage.stageName(),
            festivalStage.festivalTimes().stream()
                .map(festivalTime -> TimetableFestivalTimeResponse.of(festivalDate, festivalTime))
                .toList()
        );
    }
}
