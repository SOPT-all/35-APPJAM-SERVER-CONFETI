package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;

public record PerformanceByArtistDTO(
        long totalCount,
        List<PerformanceByArtistDetailDTO> performances
) {
    public static PerformanceByArtistDTO of(final long totalCount, final List<PerformanceByArtistDetailDTO> performances) {
        return new PerformanceByArtistDTO(
                totalCount,
                performances
        );
    }
}