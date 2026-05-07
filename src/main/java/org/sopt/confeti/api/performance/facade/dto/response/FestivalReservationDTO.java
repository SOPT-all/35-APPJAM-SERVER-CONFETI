package org.sopt.confeti.api.performance.facade.dto.response;

import lombok.Builder;
import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationFileInfo;
import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationUrl;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorInfo;

@Builder
public record FestivalReservationDTO(
    long reservationId,
    String url,
    TicketVendorInfo ticketVendor
) {

    public static FestivalReservationDTO of(FestivalReservationUrl reservation, FestivalReservationFileInfo fileInfo) {
        TicketVendorDto vendorDto = TicketVendorDto.from(reservation.getTicketVendor());
        return FestivalReservationDTO.builder()
            .reservationId(reservation.getId())
            .url(reservation.getReservationUrl())
            .ticketVendor(TicketVendorInfo.of(vendorDto, fileInfo.ticketVendorFileInfo()))
            .build();
    }
}
