package org.sopt.confeti.api.dummy.facade.dto.festival;

import java.util.List;

public record FestivalFilePathsDTO(
        String posterPath,
        String posterBgPath,
        String logoPath,
        List<FestivalReservationLogoPathDTO> reservationLogoPaths
) {
    public static FestivalFilePathsDTO of(String posterPath, String posterBgPath, String logoPath,
                                          List<String> reservationLogoPaths) {
        return new FestivalFilePathsDTO(
                posterPath, posterBgPath, logoPath,
                reservationLogoPaths.stream()
                        .map(FestivalReservationLogoPathDTO::from)
                        .toList()
        );
    }
}
