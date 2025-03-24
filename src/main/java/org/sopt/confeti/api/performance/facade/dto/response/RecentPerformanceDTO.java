package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDateTime;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.view.performance.Performance;
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
                concert.getTitle(),
                concert.getSubtitle(),
                concert.getStartAt(),
                concert.getPosterPath()
        );
    }

    public static RecentPerformanceDTO of(final Festival festival, final long performanceId) {
        return new RecentPerformanceDTO(
                performanceId,
                festival.getId(),
                PerformanceType.FESTIVAL,
                festival.getTitle(),
                festival.getSubtitle(),
                festival.getStartAt(),
                festival.getPosterPath()
        );
    }

    public static RecentPerformanceDTO from(final Performance performance) {
        return new RecentPerformanceDTO(
                performance.getId(),
                performance.getTypeId(),
                performance.getType(),
                performance.getTitle(),
                performance.getSubtitle(),
                performance.getStartAt(),
                performance.getPosterPath()
        );
    }
}
