package org.sopt.confeti.api.dummy.facade.dto.concert;

public record ConcertReservationLogoPathDTO(
        String logoPath
) {
    public static ConcertReservationLogoPathDTO from(String logoPath) {
        return new ConcertReservationLogoPathDTO(logoPath);
    }
}
