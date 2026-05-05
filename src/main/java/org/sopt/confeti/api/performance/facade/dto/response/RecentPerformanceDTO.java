package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceInfo;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record RecentPerformanceDTO(
        long performanceId,
        long typeId,
        PerformanceType type,
        String title,
        String area,
        LocalDate startAt,
        String posterUrl,
        boolean isFavorite
) {
    public static RecentPerformanceDTO of(final PerformanceInfo performance, final boolean isFavorite) {
        return new RecentPerformanceDTO(
                performance.id(),
                performance.typeId(),
                performance.type(),
                performance.title(),
                performance.area(),
                performance.startAt(),
                performance.posterUrl(),
                isFavorite
        );
    }

    public static RecentPerformanceDTO from(final PerformanceInfo performance) {
        return of(performance, false);
    }
}
