package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationUrl;

public record FestivalReservationDTO(
        long reservationId,
        String url,
        String name,
        String logoPath
) {

    public static FestivalReservationDTO from(FestivalReservationUrl reservation) {
        var vendor = reservation.getTicketVendor();
        return new FestivalReservationDTO(
                reservation.getId(),
                reservation.getReservationUrl(),
                vendor != null ? vendor.getName() : null,
                vendor != null ? vendor.getLogoPath() : null
        );
    }
}
