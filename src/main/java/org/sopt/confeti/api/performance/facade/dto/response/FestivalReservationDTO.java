package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationUrl;

public record FestivalReservationDTO(
        String url,
        String name,
        String logoPath
) {

    public static FestivalReservationDTO from(FestivalReservationUrl reservation) {
        return new FestivalReservationDTO(
                reservation.getReservationUrl(),
                reservation.getName(),
                reservation.getLogoPath()
        );
    }
}
