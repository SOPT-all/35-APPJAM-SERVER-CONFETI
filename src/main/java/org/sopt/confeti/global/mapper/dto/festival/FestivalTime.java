package org.sopt.confeti.global.mapper.dto.festival;

import java.time.LocalTime;
import java.util.List;
import org.sopt.confeti.domain.performance_schedule.PerformanceSchedule;

public record FestivalTime(
        LocalTime startAt,
        LocalTime endAt,
        List<FestivalArtist> artists
) {
    public static FestivalTime of(LocalTime startAt, LocalTime endAt, List<PerformanceSchedule> schedules) {
        return new FestivalTime(
                startAt,
                endAt,
                schedules.stream()
                        .map(FestivalArtist::from)
                        .toList()
        );
    }
}
