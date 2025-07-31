package org.sopt.confeti.global.mapper.dto.concert;

import org.sopt.confeti.domain.performance_schedule.PerformanceSchedule;

public record ConcertArtist(
        String artistId,
        String name,
        String profileUrl
) {
    public static ConcertArtist from(PerformanceSchedule schedule) {
        return new ConcertArtist(
                schedule.getArtist().getId(),
                schedule.getArtist().getName(),
                schedule.getArtist().getProfileUrl()
        );
    }
}
