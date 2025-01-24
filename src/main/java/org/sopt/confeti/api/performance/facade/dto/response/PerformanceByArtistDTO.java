package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.global.common.CursorPage;

public record PerformanceByArtistDTO(
        long totalCount,
        CursorPage<PerformanceByArtistDetailDTO> performanceCursorPage
) {
    public static PerformanceByArtistDTO of(final long totalCount, final CursorPage<PerformanceByArtistDetailDTO> performanceCursorPage) {
        return new PerformanceByArtistDTO(
                totalCount,
                performanceCursorPage
        );
    }
}