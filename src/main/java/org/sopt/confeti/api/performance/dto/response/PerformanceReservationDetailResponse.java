package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDetailDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.util.S3FileHandler;

public record PerformanceReservationDetailResponse(
        long index,
        long performanceId,
        PerformanceType type,
        String subtitle,
        String reserveAt,
        String reservationBgUrl
){
    public static PerformanceReservationDetailResponse of(PerformanceReservationDetailDTO performanceReservation, S3FileHandler s3FileHandler) {
        return new PerformanceReservationDetailResponse(
                performanceReservation.index(),
                performanceReservation.performanceId(),
                performanceReservation.type(),
                performanceReservation.subtitle(),
                performanceReservation.reserveAt(),
                s3FileHandler.getFileUrl(performanceReservation.reservationBgUrl())
        );
    }
}
