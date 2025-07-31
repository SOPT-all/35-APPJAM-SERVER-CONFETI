package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationUrl;
import org.sopt.confeti.domain.performance_reservation_url.PerformanceReservationUrl;

public record FestivalReservationDTO(
        String url,
        String name,
        String logoPath
) {

    public static FestivalReservationDTO from(PerformanceReservationUrl reservation) {
        return new FestivalReservationDTO(
                reservation.getReservationUrl(),
                reservation.getName(),
                reservation.getLogoPath()
        );
    }
}
