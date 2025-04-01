package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformancesDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record ArtistPerformancesResponse(
        long performanceCount,
        List<ArtistPerformancesDetailResponse> performances
) {
    public static ArtistPerformancesResponse of(ArtistPerformancesDTO artistPerformancesDTO,
                                                final S3FileHandler s3FileHandler) {
        List<ArtistPerformancesDetailResponse> performanceList = artistPerformancesDTO.performances()
                .stream()
                .map(performance -> ArtistPerformancesDetailResponse.of(performance, s3FileHandler))
                .toList();

        return new ArtistPerformancesResponse(performanceList.size(), performanceList);
    }
}
