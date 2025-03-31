package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;

public record ArtistPerformanceDTO(
        long totalCount,
        List<ArtistPerformanceDetailDTO> performances
) {
    public static ArtistPerformanceDTO of(final long totalCount, final List<ArtistPerformanceDetailDTO> performances) {
        return new ArtistPerformanceDTO(
                totalCount,
                performances
        );
    }
}