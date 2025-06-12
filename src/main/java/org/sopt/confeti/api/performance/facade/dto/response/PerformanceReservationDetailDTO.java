package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDateTime;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record PerformanceReservationDetailDTO(
        int index,
        long typeId,
        PerformanceType type,
        String title,
        LocalDateTime reserveAt
) {
    public static PerformanceReservationDetailDTO from(PerformanceTicketDTO performanceTicketDTO) {
        return new PerformanceReservationDetailDTO(
                performanceTicketDTO.index(),
                performanceTicketDTO.typeId(),
                performanceTicketDTO.type(),
                performanceTicketDTO.title(),
                performanceTicketDTO.reserveAt()
        );
    }
}