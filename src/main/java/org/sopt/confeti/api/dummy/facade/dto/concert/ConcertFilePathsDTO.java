package org.sopt.confeti.api.dummy.facade.dto.concert;

import java.util.List;

public record ConcertFilePathsDTO(
        String posterPath,
        String posterBgPath,
        List<ConcertReservationLogoPathDTO> reservationLogoPaths
) {
    public static ConcertFilePathsDTO of(String posterPath, String posterBgPath, List<String> reservationLogoPaths) {
        return new ConcertFilePathsDTO(
                posterPath, posterBgPath,
                reservationLogoPaths.stream()
                        .map(ConcertReservationLogoPathDTO::from)
                        .toList()
        );
    }
}
