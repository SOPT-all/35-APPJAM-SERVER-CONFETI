package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.concert_reservation_url.ConcertReservationUrl;

public record ConcertReservationDTO(
    String url,
    String name,
    String logoPath) {
    public static ConcertReservationDTO from(ConcertReservationUrl reservation) {
        var vendor = reservation.getTicketVendor();
        return new ConcertReservationDTO(
            reservation.getReservationUrl(),
            vendor != null ? vendor.getName() : null,
            vendor != null ? vendor.getLogoPath() : null);
    }
}
