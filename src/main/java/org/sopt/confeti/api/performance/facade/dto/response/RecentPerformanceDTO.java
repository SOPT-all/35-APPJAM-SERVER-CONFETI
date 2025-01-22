package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDateTime;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record RecentPerformanceDTO(
        long performanceId,
        long typeId,
        PerformanceType type,
        String title,
        String subtitle,
        LocalDateTime performanceAt,
        String posterPath
) {
}
