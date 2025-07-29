package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.view.performance.Performance_DPRECATED;

public record RecentPerformancesDTO(
        boolean isPersonalized,
        List<RecentPerformanceDTO> performances
) {
    public static RecentPerformancesDTO of(boolean isPersonalized, List<Performance_DPRECATED> performanceDPRECATEDS) {
        return new RecentPerformancesDTO(
                isPersonalized,
                performanceDPRECATEDS.stream()
                        .map(RecentPerformanceDTO::from)
                        .toList()
        );
    }
}
