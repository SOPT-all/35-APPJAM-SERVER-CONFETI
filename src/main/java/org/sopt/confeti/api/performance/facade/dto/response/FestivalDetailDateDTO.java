package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.performance_schedule.PerformanceSchedule;

public record FestivalDetailDateDTO(
        long festivalDateId,
        LocalDate festivalAt,
        LocalTime openAt,
        List<FestivalDetailStageDTO> stages
) {
    public static FestivalDetailDateDTO from(final PerformanceSchedule schedule) {
        return new FestivalDetailDateDTO(
                schedule.getId(),
                schedule.getFestivalAt(),
                schedule.getOpenAt(),
                schedule.getStages().stream()
                        .map(FestivalDetailStageDTO::from)
                        .toList()
        );
    }
}
