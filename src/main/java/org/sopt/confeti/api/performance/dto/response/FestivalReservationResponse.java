package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.FestivalReservationDTO;

public record FestivalReservationResponse(
        String url,
        String name,
        String logoUrl
) {
    public static FestivalReservationResponse from(FestivalReservationDTO reservationDTO) {
        return new FestivalReservationResponse(
                reservationDTO.url(),
                reservationDTO.name(),
                reservationDTO.logoUrl()
        );
    }
}
