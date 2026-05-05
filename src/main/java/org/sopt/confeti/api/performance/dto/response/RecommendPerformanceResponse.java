package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.RecommendPerformanceDTO;

public record RecommendPerformanceResponse(
    long typeId,
    String type,
    String title,
    String posterUrl
) {

    public static RecommendPerformanceResponse from(
        final RecommendPerformanceDTO recommendPerformanceDTO) {
        return new RecommendPerformanceResponse(
            recommendPerformanceDTO.typeId(),
            recommendPerformanceDTO.type().getName(),
            recommendPerformanceDTO.title(),
            recommendPerformanceDTO.posterUrl()
        );
    }
}

