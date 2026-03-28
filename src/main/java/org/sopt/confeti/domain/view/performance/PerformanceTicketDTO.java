package org.sopt.confeti.domain.view.performance;

import java.time.LocalDateTime;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record PerformanceTicketDTO(
        int index,
        long typeId,
        PerformanceType type,
        String title,
        String roundName,
        LocalDateTime reserveAt
) {
    public static PerformanceTicketDTO of(
            final int index,
            final long performanceId,
            final String type,
            final String title,
            final String roundName,
            final LocalDateTime reserveAt
    ) {
        return new PerformanceTicketDTO(
                index,
                performanceId,
                PerformanceType.convert(type),
                title,
                roundName,
                reserveAt
        );
    }
}
