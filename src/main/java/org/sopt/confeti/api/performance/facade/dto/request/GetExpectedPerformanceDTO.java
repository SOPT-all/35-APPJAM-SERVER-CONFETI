package org.sopt.confeti.api.performance.facade.dto.request;

import org.sopt.confeti.api.performance.dto.request.GetExpectedPerformanceRequest;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;

public record GetExpectedPerformanceDTO(
        long performanceId
) {
    public static GetExpectedPerformanceDTO from(GetExpectedPerformanceRequest expectedPerformanceRequest) {
        return new GetExpectedPerformanceDTO(expectedPerformanceRequest.performanceId());
    }
}
