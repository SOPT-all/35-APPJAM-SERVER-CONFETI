package org.sopt.confeti.domain.view.performance.application.dto.response;

import org.sopt.confeti.domain.view.performance.Performance_DPRECATED;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record GetPerformanceIdResponse(
        long performanceId,
        long typeId,
        PerformanceType type
) {
    public static GetPerformanceIdResponse from(final Performance_DPRECATED performanceDPRECATED) {
        return new GetPerformanceIdResponse(
                performanceDPRECATED.getId(),
                performanceDPRECATED.getTypeId(),
                performanceDPRECATED.getType()
        );
    }
}
