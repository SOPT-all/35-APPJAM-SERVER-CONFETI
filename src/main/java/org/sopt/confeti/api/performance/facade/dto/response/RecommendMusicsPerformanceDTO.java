package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.Performance;

public record RecommendMusicsPerformanceDTO(
        Long id,
        String title
) {
    public static RecommendMusicsPerformanceDTO from(Performance performance) {
        return new RecommendMusicsPerformanceDTO(
                performance.getId(),
                performance.getTitle()
        );
    }
}
