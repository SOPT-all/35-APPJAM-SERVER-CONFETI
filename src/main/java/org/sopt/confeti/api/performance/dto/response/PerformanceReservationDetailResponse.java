package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDetailDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.util.DateConvertor;

public record PerformanceReservationDetailResponse(
        int index,
        long typeId,
        PerformanceType type,
        String subtitle,
        String reserveAt
){
    public static PerformanceReservationDetailResponse from(PerformanceReservationDetailDTO performanceReservation) {

        return new PerformanceReservationDetailResponse(
                performanceReservation.index(),
                performanceReservation.typeId(),
                performanceReservation.type(),
                performanceReservation.subtitle(),
                DateConvertor.convertToDefaultFormat(performanceReservation.reserveAt())
        );
    }
}
