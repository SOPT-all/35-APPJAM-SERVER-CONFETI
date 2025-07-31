package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;
import org.sopt.confeti.global.mapper.dto.festival.FestivalStage;

public record FestivalDetailStageDTO(
        String name,
        int order,
        List<FestivalDetailTimeDTO> times
) {
    public static FestivalDetailStageDTO from(FestivalStage festivalStage) {
        return new FestivalDetailStageDTO(
                festivalStage.name(),
                festivalStage.order(),
                festivalStage.times().stream()
                        .map(FestivalDetailTimeDTO::from)
                        .toList()
        );
    }
}
