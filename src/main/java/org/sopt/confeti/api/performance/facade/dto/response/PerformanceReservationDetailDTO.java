package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.util.DateConvertor;

public record PerformanceReservationDetailDTO(
        int index,
        long typeId,
        PerformanceType type,
        String subtitle,
        String reserveAt,
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

    // 더미 데이터 고정값! - 스프린트 이후로 지우기
    public static PerformanceReservationDetailDTO of(final DummyPerformanceDTO dummyPerformanceDTO, final int index) {
        return new PerformanceReservationDetailDTO(
                index,
                dummyPerformanceDTO.typeId(),
                dummyPerformanceDTO.type(),
                dummyPerformanceDTO.subtitle(),
                DateConvertor.convert(dummyPerformanceDTO.reserveAt()),
                dummyPerformanceDTO.reservationBgUrl()
        );
    }
    // 더미 데이터 고정값! - 스프린트 이후로 지우기
}