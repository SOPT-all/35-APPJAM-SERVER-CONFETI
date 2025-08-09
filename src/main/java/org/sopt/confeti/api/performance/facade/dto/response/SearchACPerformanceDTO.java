package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.elastic_search.application.dto.response.SearchPerformanceResult;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;

public record SearchACPerformanceDTO(
        long id,
        String title,
        String posterPath,
        PerformanceType type
) {
    public static SearchACPerformanceDTO from(SearchPerformanceResult performanceResult) {
        return new SearchACPerformanceDTO(
                performanceResult.id(),
                performanceResult.title(),
                performanceResult.posterPath(),
                performanceResult.type()
        );
    }
}
