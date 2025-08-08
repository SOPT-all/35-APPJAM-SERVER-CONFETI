package org.sopt.confeti.domain.performance_schedule;

import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

import java.time.LocalTime;

public record SearchedPerformanceSchedule(
        Long id,
        ConfetiArtist artist,
        String performanceAt,
        String stageName,
        LocalTime openAt,
        int order,
        LocalTime startAt,
        LocalTime endAt
) {
    public static SearchedPerformanceSchedule from(PerformanceSchedule performanceSchedule) {
        return new SearchedPerformanceSchedule(
                performanceSchedule.getId(),
                performanceSchedule.getArtist(),
                performanceSchedule.getPerformanceAt().toString(),
                performanceSchedule.getStageName(),
                performanceSchedule.getOpenAt(),
                performanceSchedule.getOrder(),
                performanceSchedule.getStartAt(),
                performanceSchedule.getEndAt()
        );
    }
}
