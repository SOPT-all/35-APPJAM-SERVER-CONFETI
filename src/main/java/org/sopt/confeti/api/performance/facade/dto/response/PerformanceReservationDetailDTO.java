package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDateTime;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record PerformanceReservationDetailDTO(
        int index,
        long typeId,
        PerformanceType type,
        String title,
        LocalDateTime reserveAt,
        boolean isFavorite
) {
    public static PerformanceReservationDetailDTO of(PerformanceTicketDTO performanceTicketDTO, boolean isFavorite) {
        return new PerformanceReservationDetailDTO(
                performanceTicketDTO.index(),
                performanceTicketDTO.typeId(),
                performanceTicketDTO.type(),
                performanceTicketDTO.title(),
                performanceTicketDTO.reserveAt(),
                isFavorite
        );
    }

    public PerformanceReservationDetailDTO withIndex(int newIndex) {
        return new PerformanceReservationDetailDTO(
                newIndex,
                typeId,
                type,
                title,
                reserveAt,
                isFavorite
        );
    }
}
