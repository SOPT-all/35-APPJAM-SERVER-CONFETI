package org.sopt.confeti.global.mapper.dto.festival;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.sopt.confeti.domain.performance_schedule.PerformanceSchedule;

public record FestivalDate(
        LocalDate festivalAt,
        LocalTime openAt,
        List<FestivalStage> stages
) {
    public static FestivalDate of(LocalDate performanceDate, List<PerformanceSchedule> schedules) {
        Map<Stageblock, List<PerformanceSchedule>> scheduleMap = schedules.stream()
                .collect(Collectors.groupingBy(schedule -> new Stageblock(schedule.getStageName(), schedule.getOrder())));

        LocalTime openAt = schedules.getFirst().getOpenAt();

        return new FestivalDate(
                performanceDate,
                openAt,
                scheduleMap.keySet().stream()
                        .map(stageBlock -> FestivalStage.of(stageBlock.getStageName(), stageBlock.getOrder(), scheduleMap.get(stageBlock)))
                        .toList()
        );
    }
}
