package org.sopt.confeti.api.dummy.facade.dto.festival.request;


import org.sopt.confeti.api.dummy.dto.festival.CreateFestivalReservationUrlRequest;
import org.sopt.confeti.domain.ticketvendor.TicketVendor;

public record CreateFestivalReservationUrlDTO(
        String reservationUrl,
        String name,
        String logoPath
) {
    public static CreateFestivalReservationUrlDTO from(CreateFestivalReservationUrlRequest request, String logoPath) {
        return new CreateFestivalReservationUrlDTO(
                request.reservationUrl(),
                request.name(),
                logoPath
        );
    }
}
