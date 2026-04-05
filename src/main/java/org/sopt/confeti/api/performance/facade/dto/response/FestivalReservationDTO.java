package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationUrl;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorDto;

public record FestivalReservationDTO(
    long reservationId,
    String url,
    TicketVendorDto ticketVendor
) {

    public static FestivalReservationDTO from(FestivalReservationUrl reservation) {
        return new FestivalReservationDTO(
            reservation.getId(),
            reservation.getReservationUrl(),
            TicketVendorDto.from(reservation.getTicketVendor())
        );
    }
}
