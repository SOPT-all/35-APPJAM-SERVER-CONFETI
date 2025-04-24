package org.sopt.confeti.api.setlist.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;

public record SearchPerformancesDTO(
        List<SearchPerformanceDTO> performances
) {
    public static SearchPerformancesDTO from(List<PerformanceDTO> performances) {
        return new SearchPerformancesDTO(
                performances.stream()
                        .map(SearchPerformanceDTO::from)
                        .toList()
        );
    }
}
