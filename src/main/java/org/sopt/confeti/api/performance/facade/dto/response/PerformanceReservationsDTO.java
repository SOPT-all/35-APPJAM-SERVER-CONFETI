package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;

import org.sopt.confeti.domain.performance.Performance;

public record PerformanceReservationsDTO(
        List<PerformanceReservationDTO> performanceReservation
) {
    public static PerformanceReservationsDTO from(List<Performance> performances) {
        return new PerformanceReservationsDTO(
                performances.stream()
                        .map(PerformanceReservationDTO::from)
                        .toList()
        );
    }
}
