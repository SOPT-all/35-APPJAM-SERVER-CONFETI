package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.global.mapper.dto.concert.ConcertReservation;

public record ConcertReservationDTO(
        String url,
        String name,
        String logoUrl
) {
    public static ConcertReservationDTO from(ConcertReservation reservation) {
        return new ConcertReservationDTO(
                reservation.url(),
                reservation.name(),
                reservation.logoUrl()
        );
    }
}
