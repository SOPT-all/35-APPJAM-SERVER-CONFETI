package org.sopt.confeti.api.dummy.dto.festival;

import java.util.List;
import org.sopt.confeti.domain.festival_stage.FestivalStage;

public record FixDummyFestivalDTO(
        String title,
        List<String> stages
) {
    public static FixDummyFestivalDTO of(String title, List<FestivalStage> stages) {
        return new FixDummyFestivalDTO(
                title,
                stages.stream()
                        .map(FestivalStage::getName)
                        .toList()
        );
    }
}
