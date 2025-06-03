package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.PerformanceIdDTO;

public record PerformanceIdResponse(
        String type,
        long typeId
) {
    public static PerformanceIdResponse from(PerformanceIdDTO performanceIdDTO) {
        return new PerformanceIdResponse(
                performanceIdDTO.type().getType().toUpperCase(),
                performanceIdDTO.typeId()
        );
    }
}