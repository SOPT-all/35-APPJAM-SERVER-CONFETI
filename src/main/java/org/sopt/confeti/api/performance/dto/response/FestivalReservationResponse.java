package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.FestivalReservationDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record FestivalReservationResponse(
    String url,
    String name,
    String logoUrl
) {

    public static FestivalReservationResponse of(FestivalReservationDTO reservationDTO,
        S3FileHandler s3FileHandler) {
        return new FestivalReservationResponse(
            reservationDTO.url(),
            reservationDTO.name(),
            s3FileHandler.getFileUrl(
                FolderPath.combine(FolderPath.TICKET_VENDOR, FolderPath.LOGO),
                reservationDTO.logoPath()).toString()
        );
    }
}
