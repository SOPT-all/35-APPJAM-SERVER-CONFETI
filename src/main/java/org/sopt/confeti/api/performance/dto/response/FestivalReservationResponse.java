package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.FestivalReservationDTO;

public record FestivalReservationResponse(
    long reservationId,
    String url,
    TicketVendorResponse ticketVendorResponse
) {

    public static FestivalReservationResponse from(FestivalReservationDTO reservationDTO) {
        return new FestivalReservationResponse(
            reservationDTO.reservationId(),
            reservationDTO.url(),
            TicketVendorResponse.from(reservationDTO.ticketVendor())
        );
    }
}
