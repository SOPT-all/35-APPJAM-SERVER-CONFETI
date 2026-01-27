package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;

public record PerformanceReservationDTO(
    boolean isPersonalized,
    List<PerformanceReservationDetailDTO> performanceReservation
) {

    public static PerformanceReservationDTO of(
        boolean isPersonalized,
        List<PerformanceTicketDTO> performanceTickets) {
        return new PerformanceReservationDTO(
            isPersonalized,
            performanceTickets.stream()
                .map(PerformanceReservationDetailDTO::from)
                .toList()
        );
    }
}
