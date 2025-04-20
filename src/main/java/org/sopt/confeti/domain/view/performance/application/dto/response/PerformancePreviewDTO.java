package org.sopt.confeti.domain.view.performance.application.dto.response;

import org.sopt.confeti.global.common.constant.PerformanceType;

public record PerformancePreviewDTO(
        long typeId,
        PerformanceType type,
        String title,
        String posterPath
) {
    public static PerformancePreviewDTO of(
            final long typeId,
            final String type,
            final String title,
            final String posterPath
    ) {
        return new PerformancePreviewDTO(
                typeId,
                PerformanceType.convert(type),
                title,
                posterPath
        );
    }
}
