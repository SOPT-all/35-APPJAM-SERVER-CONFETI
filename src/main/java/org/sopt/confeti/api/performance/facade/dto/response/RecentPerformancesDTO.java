package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.view.performance.Performance;

public record RecentPerformancesDTO(
        boolean isPersonalized,
        List<RecentPerformanceDTO> performances
) {
    public static RecentPerformancesDTO of(boolean isPersonalized, List<Performance> performances) {
        return new RecentPerformancesDTO(
                isPersonalized,
                performances.stream()
                        .map(RecentPerformanceDTO::from)
                        .toList()
        );
    }
}
