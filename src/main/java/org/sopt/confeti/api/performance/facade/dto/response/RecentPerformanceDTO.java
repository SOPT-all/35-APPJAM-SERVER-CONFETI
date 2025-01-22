package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDateTime;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record RecentPerformanceDTO(
        long performanceId,
        long typeId,
        PerformanceType type,
        String title,
        String subtitle,
        LocalDateTime performanceAt,
        String posterPath
) {
    public static RecentPerformanceDTO of(final Concert concert, final long performanceId) {
        return new RecentPerformanceDTO(
                performanceId,
                concert.getId(),
                PerformanceType.CONCERT,
                concert.getConcertTitle(),
                concert.getConcertSubtitle(),
                concert.getConcertStartAt(),
                concert.getConcertPosterPath()
        );
    }

    public static RecentPerformanceDTO of(final Festival festival, final long performanceId) {
        return new RecentPerformanceDTO(
                performanceId,
                festival.getId(),
                PerformanceType.CONCERT,
                festival.getFestivalTitle(),
                festival.getFestivalSubtitle(),
                festival.getFestivalStartAt(),
                festival.getFestivalPosterPath()
        );
    }
}
