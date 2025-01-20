package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record PerformanceReservationDetailDTO(
        long index,
        long performanceId,
        PerformanceType type,
        String subtitle,
        String reserveAt,
        String reservationBgUrl
) {
    public static PerformanceReservationDetailDTO from(PerformanceTicketDTO performanceTicketDTO) {
        return new PerformanceReservationDetailDTO(
                performanceTicketDTO.index(),
                performanceTicketDTO.performanceId(),
                performanceTicketDTO.type(),
                performanceTicketDTO.subtitle(),
                performanceTicketDTO.reserveAt(),
                performanceTicketDTO.reservationBgUrl()
        );
    }
}