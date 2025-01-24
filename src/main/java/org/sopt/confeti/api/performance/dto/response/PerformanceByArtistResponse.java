package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.PerformanceByArtistDTO;
import org.sopt.confeti.global.util.S3FileHandler;

import java.util.List;

public record PerformanceByArtistResponse(
        long nextCursor,
        long performanceCount,
        List<PerformanceByArtistDetailResponse> performanceByArtistDTO
) {
    private static final long DEFAULT_NEXT_CURSOR = -1L;

    public static PerformanceByArtistResponse of(PerformanceByArtistDTO performanceByArtistDTO, final S3FileHandler s3FileHandler) {
        Long nextCursor = DEFAULT_NEXT_CURSOR;

        if (!performanceByArtistDTO.cursorPage().isLast()) {
            nextCursor = performanceByArtistDTO.cursorPage().getNextCursor().performanceId();
        }

        return new PerformanceByArtistResponse(
                nextCursor,
                performanceByArtistDTO.totalCount(),
                performanceByArtistDTO.cursorPage().getItems()
                        .stream()
                        .map(performances -> PerformanceByArtistDetailResponse.of(performances, s3FileHandler))
                        .toList()
        );
    }
}
