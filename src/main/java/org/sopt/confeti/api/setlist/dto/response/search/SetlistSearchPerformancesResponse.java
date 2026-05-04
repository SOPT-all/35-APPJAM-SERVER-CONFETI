package org.sopt.confeti.api.setlist.dto.response.search;

import java.util.List;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SearchPerformancesDTO;

public record SetlistSearchPerformancesResponse(
    int performanceCount,
    List<SetlistSearchPerformanceResponse> performances
) {

    public static SetlistSearchPerformancesResponse from(SearchPerformancesDTO performancesDTO) {
        return new SetlistSearchPerformancesResponse(
            performancesDTO.performances().size(),
            performancesDTO.performances().stream()
                .map(SetlistSearchPerformanceResponse::from)
                .toList()
        );
    }
}
