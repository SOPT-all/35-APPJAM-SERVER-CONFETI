package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.view.performance.Performance_DPRECATED;

public record RecommendPerformancesDTO(
        List<RecommendPerformanceDTO> performances
) {
    public static RecommendPerformancesDTO from(List<Performance_DPRECATED> performanceDPRECATEDS) {
        return new RecommendPerformancesDTO(
                performanceDPRECATEDS.stream()
                        .map(RecommendPerformanceDTO::from)
                        .toList()
        );
    }
}