package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.global.mapper.dto.festival.FestivalReservation;

public record FestivalReservationDTO(
        String url,
        String name,
        String logoUrl
) {

    public static FestivalReservationDTO from(FestivalReservation reservation) {
        return new FestivalReservationDTO(
                reservation.url(),
                reservation.name(),
                reservation.logoUrl()
        );
    }
}
