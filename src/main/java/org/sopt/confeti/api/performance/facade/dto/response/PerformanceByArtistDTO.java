package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.global.common.CursorPage;

public record PerformanceByArtistDTO(
        long totalCount,
        CursorPage<PerformanceByArtistDetailDTO> cursorPage
) {
    public static PerformanceByArtistDTO of(final long totalCount, final CursorPage<PerformanceByArtistDetailDTO> cursorPage) {
        return new PerformanceByArtistDTO(
                totalCount,
                cursorPage
        );
    }
}