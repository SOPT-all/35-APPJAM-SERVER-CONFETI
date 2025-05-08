package org.sopt.confeti.api.dummy.facade.dto.festival;

import java.util.List;

public record FestivalFilePathsDTO(
        String posterPath,
        String logoPath,
        List<FestivalReservationLogoPathDTO> reservationLogoPaths
) {
    public static FestivalFilePathsDTO of(String posterPath, String logoPath,
                                          List<String> reservationLogoPaths) {
        return new FestivalFilePathsDTO(
                posterPath, logoPath,
                reservationLogoPaths.stream()
                        .map(FestivalReservationLogoPathDTO::from)
                        .toList()
        );
    }
}
