package org.sopt.confeti.domain.view.performance.application.dto.response;

import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record GetPerformanceIdResponse(
        long performanceId,
        long typeId,
        PerformanceType type
) {
    public static GetPerformanceIdResponse from(final Performance performance) {
        return new GetPerformanceIdResponse(
                performance.getId(),
                performance.getTypeId(),
                performance.getType()
        );
    }
}
