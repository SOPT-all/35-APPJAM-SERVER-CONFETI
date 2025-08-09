package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;

public record RecentPerformanceDTO(
        long performanceId,
        PerformanceType type,
        String title,
        LocalDate startAt,
        String posterPath
) {
    public static RecentPerformanceDTO of(final Concert concert, final long performanceId) {
        return new RecentPerformanceDTO(
                performanceId,
                PerformanceType.CONCERT,
                concert.getTitle(),
                concert.getStartAt(),
                concert.getPosterPath()
        );
    }

    public static RecentPerformanceDTO of(final Festival festival, final long performanceId) {
        return new RecentPerformanceDTO(
                performanceId,
                PerformanceType.FESTIVAL,
                festival.getTitle(),
                festival.getStartAt(),
                festival.getPosterPath()
        );
    }

    public static RecentPerformanceDTO from(final Performance performance) {
        return new RecentPerformanceDTO(
                performance.getId(),
                performance.getType(),
                performance.getTitle(),
                performance.getStartAt(),
                performance.getPosterPath()
        );
    }
}
