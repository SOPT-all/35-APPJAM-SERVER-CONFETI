package org.sopt.confeti.api.performance.dto.request;

import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;

public record GetExpectedPerformanceRequest(
        PerformanceType_DEPRECATED type,
        long typeId
) {
    public static GetExpectedPerformanceRequest of(PerformanceType_DEPRECATED type, long typeId) {
        return new GetExpectedPerformanceRequest(
                type,
                typeId
        );
    }
}
