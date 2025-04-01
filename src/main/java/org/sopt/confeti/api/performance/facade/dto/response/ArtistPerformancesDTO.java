package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;

public record ArtistPerformancesDTO(
        List<ArtistPerformancesDetailDTO> performances
) {
    public static ArtistPerformancesDTO from(final List<ArtistPerformancesDetailDTO> performances) {
        return new ArtistPerformancesDTO(
                performances
        );
    }
}