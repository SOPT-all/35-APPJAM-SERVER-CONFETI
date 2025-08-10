package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDTO;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;
import org.sopt.confeti.global.util.DateConvertor;

public record PerformanceReservationResponse(
        long performanceId,
        PerformanceType type,
        String title,
        String reserveAt
) {
    public static PerformanceReservationResponse from(PerformanceReservationDTO performanceReservation) {
        return new PerformanceReservationResponse(
                performanceReservation.id(),
                performanceReservation.type(),
                performanceReservation.title(),
                DateConvertor.convertToDefaultFormat(performanceReservation.reserveAt())
        );
    }
}
