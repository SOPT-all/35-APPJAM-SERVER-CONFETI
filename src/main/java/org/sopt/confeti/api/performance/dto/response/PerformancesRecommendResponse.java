package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.PerformancesRecommendDTO;

public record PerformancesRecommendResponse(
        List<PerformanceRecommendResponse> performances
) {
    public static PerformancesRecommendResponse from(PerformancesRecommendDTO performances) {
        return new PerformancesRecommendResponse(
                performances.performances().stream()
                        .map(PerformanceRecommendResponse::from)
                        .toList()
        );
    }
}
