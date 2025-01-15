package org.sopt.confeti.api.dto.response;

import java.time.LocalDateTime;

public record PerformanceReservationResponse(
        Integer index,
        Long performanceId,
        String type,
        String subtitle,
        LocalDateTime reserveAt,
        String reservationBgUrl
) {}
