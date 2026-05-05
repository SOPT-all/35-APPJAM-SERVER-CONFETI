package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.UpcomingPerformancesDTO;

public record UpcomingPerformancesResponse(
    List<UpcomingPerformanceResponse> performances
) {

    public static UpcomingPerformancesResponse from(
        UpcomingPerformancesDTO upcomingPerformancesDTO) {
        return new UpcomingPerformancesResponse(
            upcomingPerformancesDTO.performances().stream()
                .map(UpcomingPerformanceResponse::from)
                .toList()
        );
    }
}
