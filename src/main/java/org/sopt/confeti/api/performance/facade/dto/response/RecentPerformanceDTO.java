package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record RecentPerformanceDTO(
        long performanceId,
        long typeId,
        PerformanceType type,
        String title,
        String area,
        LocalDate startAt,
        String posterPath,
        boolean isFavorite
) {
    public static RecentPerformanceDTO of(final Performance performance, final boolean isFavorite) {
        return new RecentPerformanceDTO(
                performance.getId(),
                performance.getTypeId(),
                performance.getType(),
                performance.getTitle(),
                performance.getArea(),
                performance.getStartAt(),
                performance.getPosterPath(),
                isFavorite
        );
    }

    public static RecentPerformanceDTO from(final Performance performance) {
        return of(performance, false);
    }
}
