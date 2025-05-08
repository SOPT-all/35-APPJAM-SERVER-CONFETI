package org.sopt.confeti.api.dummy.facade.dto.concert;

import java.util.List;

public record ConcertFilePathsDTO(
        String posterPath,
        List<ConcertReservationLogoPathDTO> reservationLogoPaths
) {
    public static ConcertFilePathsDTO of(String posterPath, List<String> reservationLogoPaths) {
        return new ConcertFilePathsDTO(
                posterPath,
                reservationLogoPaths.stream()
                        .map(ConcertReservationLogoPathDTO::from)
                        .toList()
        );
    }
}
