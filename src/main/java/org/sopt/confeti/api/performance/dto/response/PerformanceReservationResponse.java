package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDTO;
import org.sopt.confeti.global.util.S3FileHandler;

import java.util.List;

public record PerformanceReservationResponse(
        int performanceCount,
        List<PerformanceReservationDetailResponse> performances
){
    public static PerformanceReservationResponse from(final PerformanceReservationDTO performanceReservation) {
        return new PerformanceReservationResponse(
                performanceReservation.performanceReservation().size(),
                performanceReservation.performanceReservation().stream()
                        .map(PerformanceReservationDetailResponse::from)
                        .toList()
        );
    }
}
