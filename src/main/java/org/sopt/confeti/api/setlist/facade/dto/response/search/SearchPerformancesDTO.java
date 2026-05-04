package org.sopt.confeti.api.setlist.facade.dto.response.search;

import java.util.List;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceInfo;

public record SearchPerformancesDTO(
    List<SearchPerformanceDTO> performances
) {

    public static SearchPerformancesDTO from(List<PerformanceInfo> performances) {
        return new SearchPerformancesDTO(
            performances.stream()
                .map(SearchPerformanceDTO::from)
                .toList()
        );
    }
}
