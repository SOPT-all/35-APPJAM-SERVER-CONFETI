package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationsDTO;

public record PerformanceReservationsResponse(
        int performanceCount,
        List<PerformanceReservationResponse> performances
) {
    public static PerformanceReservationsResponse from(final PerformanceReservationsDTO performanceReservation) {
        return new PerformanceReservationsResponse(
                performanceReservation.performanceReservation().size(),
                performanceReservation.performanceReservation().stream()
                        .map(PerformanceReservationResponse::from)
                        .toList()
        );
    }
}
