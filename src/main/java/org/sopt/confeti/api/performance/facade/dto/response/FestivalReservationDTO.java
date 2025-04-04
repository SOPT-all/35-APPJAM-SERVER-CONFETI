package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationUrl;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record FestivalReservationDTO(
        String url,
        String name,
        String logoUrl
) {

    public static FestivalReservationDTO of(FestivalReservationUrl reservation, S3FileHandler s3FileHandler) {
        return new FestivalReservationDTO(
                reservation.getReservationUrl(),
                reservation.getName(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.FESTIVAL, FolderPath.RESERVATION, FolderPath.LOGO),
                        reservation.getLogoPath()).toString()
        );
    }
}
