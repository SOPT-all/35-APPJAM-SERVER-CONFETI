package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDTO;

public record PerformanceReservationResponse(
        int performanceCount,
        List<PerformanceReservationDetailResponse> performances
) {
    public static PerformanceReservationResponse from(final PerformanceReservationDTO performanceReservation) {
        return new PerformanceReservationResponse(
                performanceReservation.performanceReservation().size(),
                performanceReservation.performanceReservation().stream()
                        .map(PerformanceReservationDetailResponse::from)
                        .toList()
        );
    }
}
