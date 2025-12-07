package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.Performance;

public record RecommendSongsPerformanceDTO(
    Long id,
    String title
) {

    public static RecommendSongsPerformanceDTO from(Performance performance) {
        return new RecommendSongsPerformanceDTO(
            performance.getId(),
            performance.getTitle()
        );
    }
}
