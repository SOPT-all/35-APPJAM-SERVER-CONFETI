package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendPerformancesDTO;

public record RecommendPerformancesResponse(
    List<RecommendPerformanceResponse> performances
) {

    public static RecommendPerformancesResponse from(
        final RecommendPerformancesDTO recommendPerformancesDTO) {
        return new RecommendPerformancesResponse(
            recommendPerformancesDTO.performances().stream()
                .map(RecommendPerformanceResponse::from)
                .toList()
        );
    }
}

