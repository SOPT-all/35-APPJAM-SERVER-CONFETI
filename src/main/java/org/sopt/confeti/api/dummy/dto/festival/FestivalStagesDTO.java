package org.sopt.confeti.api.dummy.dto.festival;

import java.util.List;
import org.sopt.confeti.domain.festival_stage.FestivalStage;

public record FestivalStagesDTO(
        List<String> stages
) {
    public static FestivalStagesDTO from(List<FestivalStage> stages) {
        return new FestivalStagesDTO(
                stages.stream()
                        .map(FestivalStage::getName)
                        .toList()
        );
    }
}
