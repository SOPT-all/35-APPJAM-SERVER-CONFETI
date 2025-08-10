package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendPerformancesDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record RecommendPerformancesResponse(
        List<RecommendPerformanceResponse> performances
) {
    public static RecommendPerformancesResponse from(RecommendPerformancesDTO recommendPerformancesDTO) {
        return new RecommendPerformancesResponse(
                recommendPerformancesDTO.performances().stream()
                        .map(RecommendPerformanceResponse::from)
                        .toList()
        );
    }
}

