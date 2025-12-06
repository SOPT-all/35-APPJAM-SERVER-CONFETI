package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.RecommendSongsPerformanceDTO;

@Deprecated
public record RecommendMusicsPerformanceResponse_deprecated(
    Long performanceId,
    String title
) {

    public static RecommendMusicsPerformanceResponse_deprecated from(
        RecommendSongsPerformanceDTO recommendMusicsDTO) {
        return new RecommendMusicsPerformanceResponse_deprecated(
            recommendMusicsDTO.id(),
            recommendMusicsDTO.title()
        );
    }
}
