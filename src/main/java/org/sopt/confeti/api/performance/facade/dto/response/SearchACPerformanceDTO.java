package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.elastic_search.application.dto.response.SearchPerformanceResult;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record SearchACPerformanceDTO(
    long id,
    String title,
    String posterUrl,
    PerformanceType type
) {

    public static SearchACPerformanceDTO of(SearchPerformanceResult performanceResult,
        String posterUrl) {
        return new SearchACPerformanceDTO(
            performanceResult.id(),
            performanceResult.title(),
            posterUrl,
            performanceResult.type()
        );
    }
}
