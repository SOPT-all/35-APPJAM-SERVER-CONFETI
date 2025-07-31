package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.sopt.confeti.domain.performance_schedule.PerformanceSchedule;
import org.sopt.confeti.global.mapper.dto.festival.FestivalDate;

public record FestivalDetailDateDTO(
        LocalDate festivalAt,
        LocalTime openAt,
        List<FestivalDetailStageDTO> stages
) {
    public static FestivalDetailDateDTO from(FestivalDate festivalDate) {
        return new FestivalDetailDateDTO(
                festivalDate.festivalAt(),
                festivalDate.openAt(),
                festivalDate.stages().stream()
                        .map(FestivalDetailStageDTO::from)
                        .toList()
        );
    }
}
