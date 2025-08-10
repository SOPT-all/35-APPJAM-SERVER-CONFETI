package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDateTime;

import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.performance.PerformanceType;

public record PerformanceReservationDTO(
        long id,
        PerformanceType type,
        String title,
        LocalDateTime reserveAt
) {
    public static PerformanceReservationDTO from(Performance performance) {
        return new PerformanceReservationDTO(
                performance.getId(),
                performance.getType(),
                performance.getTitle(),
                performance.getReserveAt()
        );
    }
}