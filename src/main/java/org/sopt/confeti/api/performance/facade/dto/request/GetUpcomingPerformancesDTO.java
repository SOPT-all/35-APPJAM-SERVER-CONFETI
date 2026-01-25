package org.sopt.confeti.api.performance.facade.dto.request;

import java.util.List;
import org.sopt.confeti.api.performance.dto.request.GetUpcomingPerformanceRequest;

public record GetUpcomingPerformancesDTO(
        List<GetUpcomingPerformanceDTO> upcomingPerformanceDTOs
) {
    public static GetUpcomingPerformancesDTO from(List<GetUpcomingPerformanceRequest> upcomingPerformanceRequests) {
        return new GetUpcomingPerformancesDTO(
                upcomingPerformanceRequests.stream()
                        .map(GetUpcomingPerformanceDTO::from)
                        .toList()
        );
    }
}
