package org.sopt.confeti.api.dummy.facade.dto.concert.request;

import org.sopt.confeti.api.dummy.dto.concert.CreateConcertReservationUrlRequest;
import org.sopt.confeti.api.dummy.facade.dto.concert.ConcertReservationLogoPathDTO;

public record CreateConcertReservationUrlDTO(
        String reservationUrl,
        String name,
        String logoPath
) {
    public static CreateConcertReservationUrlDTO of(CreateConcertReservationUrlRequest request, ConcertReservationLogoPathDTO logoPath) {
        return new CreateConcertReservationUrlDTO(
                request.getReservationUrl(),
                request.getName(),
                logoPath.logoPath()
        );
    }
}
