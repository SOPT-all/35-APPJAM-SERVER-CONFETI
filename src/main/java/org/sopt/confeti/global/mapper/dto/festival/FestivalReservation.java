package org.sopt.confeti.global.mapper.dto.festival;

import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.domain.performance_reservation_url.PerformanceReservationUrl;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record FestivalReservation(
        String url,
        String name,
        String logoPath
) {
    public static FestivalReservation of(PerformanceReservationUrl reservationUrl, S3FileHandler s3FileHandler) {
        return new FestivalReservation(
                reservationUrl.getReservationUrl(),
                reservationUrl.getName(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.getFolderPathByPerformanceType(PerformanceType.FESTIVAL), FolderPath.RESERVATION, FolderPath.LOGO),
                        reservationUrl.getLogoPath()
                ).toString()
        );
    }
}
