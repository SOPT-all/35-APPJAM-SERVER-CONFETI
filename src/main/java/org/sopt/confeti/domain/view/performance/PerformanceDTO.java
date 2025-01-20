package org.sopt.confeti.domain.view.performance;

import org.sopt.confeti.global.common.constant.PerformanceType;

public record PerformanceDTO(
        long performanceId,
        PerformanceType type,
        String title,
        String posterPath
) {
    public PerformanceDTO(
            final long performanceId,
            final String type,
            final String title,
            final String posterPath
    ) {
        this(
                performanceId,
                PerformanceType.convert(type),
                title,
                posterPath
        );
    }
}
