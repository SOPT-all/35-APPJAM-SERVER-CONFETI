package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;

public record PerformancesRecommendDTO(
        List<PerformanceRecommendDTO> performances
) {
    public static PerformancesRecommendDTO from(List<PerformanceRecommendDTO> performancesRecommend) {
        return new PerformancesRecommendDTO(performancesRecommend);
    }
}
