package org.sopt.confeti.api.performance.facade.dto.request;

import org.sopt.confeti.api.performance.dto.request.GetUpcomingPerformanceRequest;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record GetUpcomingPerformanceDTO(
        PerformanceType type,
        long typeId
) {
    public static GetUpcomingPerformanceDTO from(GetUpcomingPerformanceRequest upcomingPerformanceRequest) {
        return new GetUpcomingPerformanceDTO(
                upcomingPerformanceRequest.type(),
                upcomingPerformanceRequest.typeId()
        );
    }
}
