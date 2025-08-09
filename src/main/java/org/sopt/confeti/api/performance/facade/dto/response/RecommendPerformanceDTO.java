package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.domain.view.performance.Performance_DPRECATED;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;

public record RecommendPerformanceDTO(
        long typeId,
        PerformanceType type,
        String title,
        String posterPath
) {
    public static RecommendPerformanceDTO from(Performance_DPRECATED performanceDPRECATED) {
        return new RecommendPerformanceDTO(
                performanceDPRECATED.getTypeId(),
                performanceDPRECATED.getType(),
                performanceDPRECATED.getTitle(),
                performanceDPRECATED.getPosterPath()
        );
    }
}