package org.sopt.confeti.domain.view.performance;

import java.time.LocalDate;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record PerformanceDTO(
        long performanceId,
        PerformanceType type,
        String title,
        String posterPath
) {
    public static PerformanceDTO of(
            final long performanceId,
            final String type,
            final String title,
            final String posterPath
    ) {
        return new PerformanceDTO(
                performanceId,
                PerformanceType.convert(type),
                title,
                posterPath
        );
    }
}
