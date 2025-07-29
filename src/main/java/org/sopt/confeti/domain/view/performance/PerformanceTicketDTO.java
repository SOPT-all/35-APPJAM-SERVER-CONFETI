package org.sopt.confeti.domain.view.performance;

import java.time.LocalDateTime;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;

public record PerformanceTicketDTO(
        int index,
        long typeId,
        PerformanceType_DEPRECATED type,
        String title,
        LocalDateTime reserveAt
) {
    public static PerformanceTicketDTO of(
            final int index,
            final long performanceId,
            final String type,
            final String title,
            final LocalDateTime reserveAt
    ) {
        return new PerformanceTicketDTO(
                index,
                performanceId,
                PerformanceType_DEPRECATED.convert(type),
                title,
                reserveAt
        );
    }
}