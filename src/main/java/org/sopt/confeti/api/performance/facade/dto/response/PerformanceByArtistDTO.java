package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.global.common.CursorPage;

public record PerformanceByArtistDTO (
        long totalCount,
        CursorPage<PerformanceByArtistListDTO> cursorPage
) {
}