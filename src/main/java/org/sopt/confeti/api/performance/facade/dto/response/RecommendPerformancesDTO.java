package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.view.performance.Performance;

public record RecommendPerformancesDTO(
        List<RecommendPerformanceDTO> performances
) {
    public static RecommendPerformancesDTO from(List<Performance> performances) {
        return new RecommendPerformancesDTO(
                performances.stream()
                        .map(RecommendPerformanceDTO::from)
                        .toList()
        );
    }
}