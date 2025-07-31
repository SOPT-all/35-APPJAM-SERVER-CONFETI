package org.sopt.confeti.global.mapper.dto.concert;

import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.domain.performance_reservation_url.PerformanceReservationUrl;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record ConcertReservation(
        String url,
        String name,
        String logoUrl
) {
    public static ConcertReservation of(PerformanceReservationUrl reservationUrl, S3FileHandler s3FileHandler) {
        return new ConcertReservation(
                reservationUrl.getReservationUrl(),
                reservationUrl.getName(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.getFolderPathByPerformanceType(PerformanceType.CONCERT), FolderPath.RESERVATION, FolderPath.LOGO),
                        reservationUrl.getLogoPath()
                ).toString()
        );
    }
}
