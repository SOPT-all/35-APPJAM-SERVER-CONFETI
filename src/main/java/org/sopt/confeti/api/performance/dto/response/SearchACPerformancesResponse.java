package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.SearchACPerformancesDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record SearchACPerformancesResponse(
        List<SearchACPerformanceResponse> performances
) {
    public static SearchACPerformancesResponse of(SearchACPerformancesDTO performancesDTO,
                                                  S3FileHandler s3FileHandler) {
        return new SearchACPerformancesResponse(
                performancesDTO.performances().stream()
                        .map(performanceResult -> SearchACPerformanceResponse.of(performanceResult, s3FileHandler))
                        .toList()
        );
    }
}
