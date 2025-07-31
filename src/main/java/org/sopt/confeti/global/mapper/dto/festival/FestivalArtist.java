package org.sopt.confeti.global.mapper.dto.festival;

import org.sopt.confeti.domain.performance_schedule.PerformanceSchedule;

public record FestivalArtist(
        String artistId,
        String name,
        String profileUrl
) {
    public static FestivalArtist from(PerformanceSchedule schedule) {
        return new FestivalArtist(
                schedule.getArtist().getId(),
                schedule.getArtist().getName(),
                schedule.getArtist().getProfileUrl()
        );
    }
}
