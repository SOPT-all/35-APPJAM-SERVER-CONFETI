package org.sopt.confeti.api.performance.facade.dto.request;

import org.sopt.confeti.api.performance.dto.request.GetExpectedPerformanceRequest;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record GetExpectedPerformanceDTO(
        PerformanceType type,
        long typeId
) {
    public static GetExpectedPerformanceDTO from(GetExpectedPerformanceRequest expectedPerformanceRequest) {
        return new GetExpectedPerformanceDTO(
                expectedPerformanceRequest.type(),
                expectedPerformanceRequest.typeId()
        );
    }
}
