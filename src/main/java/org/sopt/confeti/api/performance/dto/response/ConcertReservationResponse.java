package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.ConcertReservationDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record ConcertReservationResponse(
    long reservationId,
    String url,
    String name,
    String logoUrl
) {

    public static ConcertReservationResponse of(ConcertReservationDTO reservationDTO,
        S3FileHandler s3FileHandler) {
        return new ConcertReservationResponse(
            reservationDTO.reservationId(),
            reservationDTO.url(),
            reservationDTO.name(),
            s3FileHandler.getFileUrl(
                FolderPath.combine(FolderPath.TICKET_VENDOR, FolderPath.LOGO),
                reservationDTO.logoPath()).toString()
        );
    }
}
