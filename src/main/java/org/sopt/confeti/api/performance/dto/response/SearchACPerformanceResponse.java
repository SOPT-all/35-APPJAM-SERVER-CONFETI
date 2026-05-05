package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.SearchACPerformanceDTO;

public record SearchACPerformanceResponse(
    long id,
    String title,
    String posterUrl
) {

    public static SearchACPerformanceResponse from(SearchACPerformanceDTO performanceDTO) {
        return new SearchACPerformanceResponse(
            performanceDTO.id(),
            performanceDTO.title(),
            performanceDTO.posterUrl()
        );
    }
}
