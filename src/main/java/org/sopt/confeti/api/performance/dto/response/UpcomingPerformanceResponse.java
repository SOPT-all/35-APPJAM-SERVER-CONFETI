package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.UpcomingPerformanceDTO;

public record UpcomingPerformanceResponse(
    long performanceId,
    String type,
    long typeId,
    String title,
    String posterUrl
) {

    public static UpcomingPerformanceResponse from(UpcomingPerformanceDTO upcomingPerformanceDTO) {
        return new UpcomingPerformanceResponse(
            upcomingPerformanceDTO.id(),
            upcomingPerformanceDTO.type().getName(),
            upcomingPerformanceDTO.typeId(),
            upcomingPerformanceDTO.title(),
            upcomingPerformanceDTO.posterUrl()
        );
    }
}
