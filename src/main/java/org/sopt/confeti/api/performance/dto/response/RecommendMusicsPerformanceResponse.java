package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.RecommendMusicsPerformanceDTO;

public record RecommendMusicsPerformanceResponse(
        Long performanceId,
        String title
) {
    public static RecommendMusicsPerformanceResponse from(RecommendMusicsPerformanceDTO recommendMusicsDTO) {
        return new RecommendMusicsPerformanceResponse(
                recommendMusicsDTO.id(),
                recommendMusicsDTO.title()
        );
    }
}
