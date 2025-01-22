package org.sopt.confeti.domain.view.performance;

import org.sopt.confeti.global.common.constant.PerformanceType;

public record PerformanceDTO(
        long typeId,
        PerformanceType type,
        String title,
        String posterPath
) {
    public static PerformanceDTO of(
            final long typeId,
            final String type,
            final String title,
            final String posterPath
    ) {
        return new PerformanceDTO(
                typeId,
                PerformanceType.convert(type),
                title,
                posterPath
        );
    }
}
