package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record PerformanceIdDTO(
        PerformanceType type,
        long typeId
) {
    public static PerformanceIdDTO from(Performance performance) {
        return new PerformanceIdDTO(
                performance.getType(),
                performance.getTypeId()
        );
    }
}
