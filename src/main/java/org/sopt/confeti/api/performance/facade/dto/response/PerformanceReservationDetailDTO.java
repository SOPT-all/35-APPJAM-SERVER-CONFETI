package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDateTime;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.util.DateConvertor;

public record PerformanceReservationDetailDTO(
        int index,
        long typeId,
        PerformanceType type,
        String subtitle,
        LocalDateTime reserveAt,
        String reservationBgUrl
) {
    public static PerformanceReservationDetailDTO from(PerformanceTicketDTO performanceTicketDTO) {
        return new PerformanceReservationDetailDTO(
                performanceTicketDTO.index(),
                performanceTicketDTO.typeId(),
                performanceTicketDTO.type(),
                performanceTicketDTO.subtitle(),
                performanceTicketDTO.reserveAt(),
                performanceTicketDTO.reservationBgUrl()
        );
    }
}