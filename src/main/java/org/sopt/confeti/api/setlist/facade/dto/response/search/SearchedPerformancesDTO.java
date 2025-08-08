package org.sopt.confeti.api.setlist.facade.dto.response.search;

import java.util.List;

import org.sopt.confeti.domain.performance.SearchedPerformance;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;

public record SearchedPerformancesDTO(
        List<SearchedPerformanceDTO> performances
) {
    public static SearchedPerformancesDTO from(List<SearchedPerformance> performances) {
        return new SearchedPerformancesDTO(
                performances.stream()
                        .map(SearchedPerformanceDTO::from)
                        .toList()
        );
    }
}
