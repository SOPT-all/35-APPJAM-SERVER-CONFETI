package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.festivalstage.FestivalStage;

public record FestivalDetailStageDTO(
        long festivalStageId,
        String name,
        int order,
        List<FestivalDetailTimeDTO> times
) {
    public static FestivalDetailStageDTO from(final FestivalStage festivalStage) {
        return new FestivalDetailStageDTO(
                festivalStage.getId(),
                festivalStage.getName(),
                festivalStage.getOrder(),
                festivalStage.getTimes().stream()
                        .map(FestivalDetailTimeDTO::from)
                        .toList()
        );
    }
}
