package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;

public record ArtistPerformanceDTO(
        List<ArtistPerformanceDetailDTO> performances
) {
    public static ArtistPerformanceDTO from(final List<ArtistPerformanceDetailDTO> performances) {
        return new ArtistPerformanceDTO(
                performances
        );
    }
}