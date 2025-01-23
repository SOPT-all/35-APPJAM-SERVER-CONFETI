package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.PerformanceByArtistListDTO;
import org.sopt.confeti.global.common.CursorPage;
import org.sopt.confeti.global.util.S3FileHandler;

import java.util.List;

public record PerformanceByArtistResponse(
        long nextCursor,
        List<PerformanceByArtistDetailResponse> performances
) {
    private static final long DEFAULT_NEXT_CURSOR = -1L;

    public static PerformanceByArtistResponse of(final CursorPage<PerformanceByArtistListDTO> cursorPage, final S3FileHandler s3FileHandler) {
        Long nextCursor = DEFAULT_NEXT_CURSOR;

        if (!cursorPage.isLast()) {
            nextCursor = cursorPage.getNextCursor().performanceId();
        }
        return new PerformanceByArtistResponse(
                nextCursor,
                cursorPage.getItems()
                        .stream()
                        .map(performances -> PerformanceByArtistDetailResponse.of(performances, s3FileHandler))
                        .toList()
        );
    }
}
