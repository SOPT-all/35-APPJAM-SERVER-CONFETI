package org.sopt.confeti.global.mapper.dto.festival;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.sopt.confeti.domain.performance_schedule.PerformanceSchedule;

public record FestivalStage(
        String name,
        int order,
        List<FestivalTime> times
) {
    public static FestivalStage of(String stageName, int order, List<PerformanceSchedule> schedules) {
        Map<TimeBlock, List<PerformanceSchedule>> scheduleMap = schedules.stream()
                .collect(Collectors.groupingBy(schedule -> new TimeBlock(schedule.getStartAt(), schedule.getEndAt())));

        return new FestivalStage(
                stageName,
                order,
                scheduleMap.keySet().stream()
                        .map(timeBlock -> FestivalTime.of(timeBlock.getStartAt(), timeBlock.getEndAt(), scheduleMap.get(timeBlock)))
                        .toList()
        );
    }
}
