package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;

public record RecentPerformancesDTO(
        List<RecentPerformanceDTO> performances
) {
}
