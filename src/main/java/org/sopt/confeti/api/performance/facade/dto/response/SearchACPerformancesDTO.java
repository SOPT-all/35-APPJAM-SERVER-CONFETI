package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.elastic_search.application.dto.response.SearchPerformanceResult;

public record SearchACPerformancesDTO(
        List<SearchACPerformanceDTO> performances
) {
    public static SearchACPerformancesDTO from(List<SearchPerformanceResult> performanceResults) {
        return new SearchACPerformancesDTO(
                performanceResults.stream()
                        .map(SearchACPerformanceDTO::from)
                        .toList()
        );
    }
}
