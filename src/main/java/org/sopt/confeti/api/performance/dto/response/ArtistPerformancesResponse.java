package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformancesDTO;

public record ArtistPerformancesResponse(
        long performanceCount,
        List<ArtistPerformancesDetailResponse> performances
) {
    public static ArtistPerformancesResponse from(ArtistPerformancesDTO artistPerformancesDTO) {
        List<ArtistPerformancesDetailResponse> performanceList = artistPerformancesDTO.performances()
                .stream()
                .map(ArtistPerformancesDetailResponse::from)
                .toList();

        return new ArtistPerformancesResponse(performanceList.size(), performanceList);
    }
}
