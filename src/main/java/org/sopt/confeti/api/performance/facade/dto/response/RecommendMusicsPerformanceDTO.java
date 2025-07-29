package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.Performance_DPRECATED;

public record RecommendMusicsPerformanceDTO(
        Long id,
        String title
) {
    public static RecommendMusicsPerformanceDTO from(Performance_DPRECATED performanceDPRECATED) {
        return new RecommendMusicsPerformanceDTO(
                performanceDPRECATED.getId(),
                performanceDPRECATED.getTitle()
        );
    }
}
