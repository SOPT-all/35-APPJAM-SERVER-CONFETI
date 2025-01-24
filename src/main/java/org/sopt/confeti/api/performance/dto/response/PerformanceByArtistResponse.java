package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.PerformanceByArtistDTO;
import org.sopt.confeti.global.util.S3FileHandler;

import java.util.List;

public record PerformanceByArtistResponse(
        long nextCursor,
        long performanceCount,
        List<PerformanceByArtistDetailResponse> performances
) {
    private static final long DEFAULT_NEXT_CURSOR = -1L;

    public static PerformanceByArtistResponse of(PerformanceByArtistDTO performanceByArtistDTO, final S3FileHandler s3FileHandler) {
        Long nextCursor = DEFAULT_NEXT_CURSOR;

        if (!performanceByArtistDTO.performanceCursorPage().isLast()) {
            nextCursor = performanceByArtistDTO.performanceCursorPage()
                    .getNextCursor()
                    .performanceId();
        }

        return new PerformanceByArtistResponse(
                nextCursor,
                performanceByArtistDTO.totalCount(),
                performanceByArtistDTO.performanceCursorPage().getItems()
                        .stream()
                        .map(performance -> PerformanceByArtistDetailResponse.of(performance, s3FileHandler))
                        .toList()
        );
    }
}
