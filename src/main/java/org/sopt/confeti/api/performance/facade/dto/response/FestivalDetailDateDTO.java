package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.sopt.confeti.domain.festivaldate.FestivalDate;

public record FestivalDetailDateDTO(
        long festivalDateId,
        LocalDate festivalAt,
        LocalTime openAt,
        List<FestivalDetailStageDTO> stages
) {
    public static FestivalDetailDateDTO from(final FestivalDate festivalDate) {
        return new FestivalDetailDateDTO(
                festivalDate.getId(),
                festivalDate.getFestivalAt(),
                festivalDate.getOpenAt(),
                festivalDate.getStages().stream()
                        .map(FestivalDetailStageDTO::from)
                        .toList()
        );
    }
}
