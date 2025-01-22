package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDTO;
import org.sopt.confeti.global.util.S3FileHandler;

import java.util.List;

public record PerformanceReservationResponse(
        int performanceCount,
        List<PerformanceReservationDetailResponse> performances
){
    public static PerformanceReservationResponse of(final PerformanceReservationDTO performanceReservation,
                                                    final S3FileHandler s3FileHandler) {
        return new PerformanceReservationResponse(
                performanceReservation.performanceReservation().size(),
                performanceReservation.performanceReservation().stream()
                        .map(performanceReserve -> PerformanceReservationDetailResponse.of(performanceReserve, s3FileHandler))
                        .toList()
        );
    }
}
