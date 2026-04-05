package org.sopt.confeti.api.performance.facade.dto.response;

import lombok.Builder;
import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationFileInfo;
import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationUrl;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorDto;

@Builder(toBuilder = true)
public record FestivalReservationDTO(
    long reservationId,
    String url,
    TicketVendorDto ticketVendor
) {

    public static FestivalReservationDTO from(FestivalReservationUrl reservation) {
        return FestivalReservationDTO.builder()
            .reservationId(reservation.getId())
            .url(reservation.getReservationUrl())
            .ticketVendor(TicketVendorDto.from(reservation.getTicketVendor()))
            .build();
    }

    public FestivalReservationDTO withFileUrls(FestivalReservationFileInfo fileInfo) {
        return this.toBuilder()
            .ticketVendor(this.ticketVendor.withFileUrls(fileInfo.ticketVendorFileInfo()))
            .build();
    }
}
