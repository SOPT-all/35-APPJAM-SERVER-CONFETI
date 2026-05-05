package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.SearchACPerformancesDTO;

public record SearchACPerformancesResponse(
    List<SearchACPerformanceResponse> performances
) {

    public static SearchACPerformancesResponse from(SearchACPerformancesDTO performancesDTO) {
        return new SearchACPerformancesResponse(
            performancesDTO.performances().stream()
                .map(SearchACPerformanceResponse::from)
                .toList()
        );
    }
}
