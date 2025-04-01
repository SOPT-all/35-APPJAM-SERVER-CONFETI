package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;

public record PerformanceReservationDTO(
        List<PerformanceReservationDetailDTO> performanceReservation
) {
    public static PerformanceReservationDTO from(List<PerformanceTicketDTO> performanceTickets) {
        return new PerformanceReservationDTO(
                performanceTickets.stream()
                        .map(PerformanceReservationDetailDTO::from)
                        .toList()
        );
    }
}
