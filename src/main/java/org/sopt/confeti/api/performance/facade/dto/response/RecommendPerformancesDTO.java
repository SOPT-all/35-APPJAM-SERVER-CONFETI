package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.Performance;
import java.util.List;

public record RecommendPerformancesDTO(
        List<RecommendPerformanceDTO> performances
) {
    public static RecommendPerformancesDTO from(List<Performance> performances){
        return new RecommendPerformancesDTO(
                performances.stream()
                        .map(RecommendPerformanceDTO::from)
                        .toList()
        );
    }
}