package org.sopt.confeti.api.dummy.facade.dto.festival;

public record FestivalReservationLogoPathDTO(
        String logoPath
) {
    public static FestivalReservationLogoPathDTO from(String logoPath) {
        return new FestivalReservationLogoPathDTO(logoPath);
    }
}
