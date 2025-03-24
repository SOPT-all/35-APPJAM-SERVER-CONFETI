package org.sopt.confeti.api.dummy.facade.dto.festival.request;


import org.sopt.confeti.api.dummy.dto.festival.CreateFestivalReservationUrlRequest;
import org.sopt.confeti.api.dummy.facade.dto.festival.FestivalReservationLogoPathDTO;

public record CreateFestivalReservationUrlDTO(
        String reservationUrl,
        String name,
        String logoPath
) {
    public static CreateFestivalReservationUrlDTO of(CreateFestivalReservationUrlRequest request, FestivalReservationLogoPathDTO logoPath) {
        return new CreateFestivalReservationUrlDTO(
                request.getReservationUrl(),
                request.getName(),
                logoPath.logoPath()
        );
    }
}
