package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;

public record PerformanceReservationDTO(
    List<PerformanceReservationDetailDTO> performanceReservation
) {
}
