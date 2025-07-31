package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.ConcertReservationDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record ConcertReservationResponse(
        String url,
        String name,
        String logoUrl
) {
    public static ConcertReservationResponse from(ConcertReservationDTO reservationDTO) {
        return new ConcertReservationResponse(
                reservationDTO.url(),
                reservationDTO.name(),
                reservationDTO.logoUrl()
        );
    }
}
