package org.sopt.confeti.domain.view.performance.application.dto.response;

import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;

public record PerformancePreviewDTO(
        long typeId,
        PerformanceType_DEPRECATED type,
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
                PerformanceType_DEPRECATED.convert(type),
                title,
                posterPath
        );
    }
}
