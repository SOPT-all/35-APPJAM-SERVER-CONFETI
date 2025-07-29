package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.Performance_DPRECATED;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record PerformanceIdDTO(
        PerformanceType type,
        long typeId
) {
    public static PerformanceIdDTO from(Performance_DPRECATED performanceDPRECATED) {
        return new PerformanceIdDTO(
                performanceDPRECATED.getType(),
                performanceDPRECATED.getTypeId()
        );
    }
}
