package org.sopt.confeti.api.dummy.facade.dto.concert.request;

import org.sopt.confeti.api.dummy.dto.concert.CreateConcertReservationUrlRequest;
import org.sopt.confeti.domain.ticketvendor.TicketVendor;

public record CreateConcertReservationUrlDTO(
        String reservationUrl,
        String name,
        String logoPath
) {
    public static CreateConcertReservationUrlDTO from(CreateConcertReservationUrlRequest request, String logoPath) {
        return new CreateConcertReservationUrlDTO(
                request.reservationUrl(),
                request.name(),
                logoPath
        );
    }
}
