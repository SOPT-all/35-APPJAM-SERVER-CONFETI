package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformanceDTO;
import org.sopt.confeti.global.util.S3FileHandler;

import java.util.List;

public record ArtistPerformanceResponse(
        long performanceCount,
        List<ArtistPerformanceDetailResponse> performances
) {
    public static ArtistPerformanceResponse of(ArtistPerformanceDTO artistPerformanceDTO, final S3FileHandler s3FileHandler) {
        return new ArtistPerformanceResponse(
                artistPerformanceDTO.totalCount(),
                artistPerformanceDTO.performances()
                        .stream()
                        .map(performance -> ArtistPerformanceDetailResponse.of(performance, s3FileHandler))
                        .toList()
        );
    }
}
