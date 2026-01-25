package org.sopt.confeti.api.performance.dto.request;

import org.sopt.confeti.global.common.constant.PerformanceType;

public record GetUpcomingPerformanceRequest(
        PerformanceType type,
        long typeId
) {
    public static GetUpcomingPerformanceRequest of(PerformanceType type, long typeId) {
        return new GetUpcomingPerformanceRequest(
                type,
                typeId
        );
    }
}
