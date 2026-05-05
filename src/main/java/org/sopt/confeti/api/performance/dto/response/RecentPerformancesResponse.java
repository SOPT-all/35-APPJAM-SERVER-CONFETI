package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformancesDTO;

public record RecentPerformancesResponse(
    List<RecentPerformanceResponse> performances
) {

    public static RecentPerformancesResponse from(RecentPerformancesDTO recentPerformancesDTO) {
        return new RecentPerformancesResponse(
            recentPerformancesDTO.performances().stream()
                .map(RecentPerformanceResponse::from)
                .toList()
        );
    }
}
