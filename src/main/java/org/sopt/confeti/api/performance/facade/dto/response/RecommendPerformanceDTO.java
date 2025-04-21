package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record RecommendPerformanceDTO(
        long typeId,
        PerformanceType type,
        String title,
        String posterPath
) {
    public static RecommendPerformanceDTO from(Performance performance) {
        return new RecommendPerformanceDTO(
                performance.getTypeId(),
                performance.getType(),
                performance.getTitle(),
                performance.getPosterPath()
                );
    }
}