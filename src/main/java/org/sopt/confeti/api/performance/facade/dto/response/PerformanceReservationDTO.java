package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;

import java.util.List;

public record PerformanceReservationDTO(
        List<PerformanceReservationDetailDTO> performanceReservation
) {
    public static PerformanceReservationDTO from (List<PerformanceTicketDTO> performanceTickets){
        return new PerformanceReservationDTO(
                performanceTickets.stream()
                        .map(PerformanceReservationDetailDTO::from)
                        .toList()
        );
    }

}
