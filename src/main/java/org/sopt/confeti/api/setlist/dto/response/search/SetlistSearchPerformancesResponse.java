package org.sopt.confeti.api.setlist.dto.response.search;

import java.util.List;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SearchPerformancesDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record SetlistSearchPerformancesResponse(
        int performanceCount,
        List<SetlistSearchPerformanceResponse> performances
) {
    public static SetlistSearchPerformancesResponse of(SearchPerformancesDTO performancesDTO,
                                                       S3FileHandler s3FileHandler) {
        return new SetlistSearchPerformancesResponse(
                performancesDTO.performances().size(),
                performancesDTO.performances().stream()
                        .map(performanceDTO -> SetlistSearchPerformanceResponse.of(performanceDTO, s3FileHandler))
                        .toList()
        );
    }
}
