package org.sopt.confeti.domain.view.performance;

import java.time.LocalDateTime;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record PerformanceTicketDTO(
        int index,
        long typeId,
        PerformanceType type,
        String subtitle,
        LocalDateTime reserveAt,
        String reservationBgUrl
) {
    public static PerformanceTicketDTO of(
            final int index,
            final long performanceId,
            final String type,
            final String subtitle,
            final LocalDateTime reserveAt,
            final String reservationBgUrl
    ) {
        return new PerformanceTicketDTO(
                index,
                performanceId,
                PerformanceType.convert(type),
                subtitle,
                reserveAt,
                reservationBgUrl
        );
    }
}