package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.PerformanceByArtistDTO;
import org.sopt.confeti.global.util.S3FileHandler;

import java.util.List;

public record PerformanceByArtistResponse(
        long performanceCount,
        List<PerformanceByArtistDetailResponse> performances
) {
    public static PerformanceByArtistResponse of(PerformanceByArtistDTO performanceByArtistDTO, final S3FileHandler s3FileHandler) {
        return new PerformanceByArtistResponse(
                performanceByArtistDTO.totalCount(),
                performanceByArtistDTO.performances()
                        .stream()
                        .map(performance -> PerformanceByArtistDetailResponse.of(performance, s3FileHandler))
                        .toList()
        );
    }
}
