package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.PerformanceIdDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record PerformanceIdResponse(
        PerformanceType type,
        long typeId
) {
    public static PerformanceIdResponse from(PerformanceIdDTO performanceIdDTO) {
        return new PerformanceIdResponse(
                performanceIdDTO.type(),
                performanceIdDTO.typeId()
        );
    }
}