package org.sopt.confeti.api.performance.dto.request;

import org.sopt.confeti.global.common.constant.PerformanceType;

public record GetExpectedPerformanceRequest(
        PerformanceType type,
        long typeId
) {
    public static GetExpectedPerformanceRequest of(PerformanceType type, long typeId) {
        return new GetExpectedPerformanceRequest(
                type,
                typeId
        );
    }
}
