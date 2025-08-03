package org.sopt.confeti.api.performance.dto.request;

public record GetExpectedPerformanceRequest(
        long performanceId
) {
    public static GetExpectedPerformanceRequest from(long performanceId) {
        return new GetExpectedPerformanceRequest(performanceId);
    }
}
