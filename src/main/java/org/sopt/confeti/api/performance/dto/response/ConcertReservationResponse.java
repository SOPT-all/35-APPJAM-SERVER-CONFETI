package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.ConcertReservationDTO;

public record ConcertReservationResponse(
    long reservationId,
    String url,
    TicketVendorResponse ticketVendor
) {

    public static ConcertReservationResponse from(ConcertReservationDTO reservationDTO) {
        return new ConcertReservationResponse(
            reservationDTO.reservationId(),
            reservationDTO.url(),
            TicketVendorResponse.from(reservationDTO.ticketVendor())
        );
    }
}
