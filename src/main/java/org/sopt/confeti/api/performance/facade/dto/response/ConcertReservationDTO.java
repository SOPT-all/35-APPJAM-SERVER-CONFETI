package org.sopt.confeti.api.performance.facade.dto.response;

import lombok.Builder;
import org.sopt.confeti.domain.concert_reservation_url.ConcertReservationFileInfo;
import org.sopt.confeti.domain.concert_reservation_url.ConcertReservationUrl;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorInfo;

@Builder
public record ConcertReservationDTO(
    long reservationId,
    String url,
    TicketVendorInfo ticketVendor
) {

    public static ConcertReservationDTO of(ConcertReservationUrl reservation, ConcertReservationFileInfo fileInfo) {
        TicketVendorDto vendorDto = TicketVendorDto.from(reservation.getTicketVendor());
        return ConcertReservationDTO.builder()
            .reservationId(reservation.getId())
            .url(reservation.getReservationUrl())
            .ticketVendor(TicketVendorInfo.of(vendorDto, fileInfo.ticketVendorFileInfo()))
            .build();
    }
}
