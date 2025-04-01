package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformancesDTO;
import org.sopt.confeti.global.util.S3FileHandler;

import java.util.List;

public record ArtistPerformancesResponse(
        long performanceCount,
        List<ArtistPerformancesDetailResponse> performances
) {
    public static ArtistPerformancesResponse of(ArtistPerformancesDTO artistPerformancesDTO, final S3FileHandler s3FileHandler) {
        List<ArtistPerformancesDetailResponse> performanceList = artistPerformancesDTO.performances()
                .stream()
                .map(performance -> ArtistPerformancesDetailResponse.of(performance, s3FileHandler))
                .toList();

        return new ArtistPerformancesResponse(performanceList.size(), performanceList);
    }
}
