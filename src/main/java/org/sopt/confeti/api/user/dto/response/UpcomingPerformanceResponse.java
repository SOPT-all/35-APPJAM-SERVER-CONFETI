package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UpcomingPerformanceDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.util.DateConvertor;

public record UpcomingPerformanceResponse(
    long typeId,
    PerformanceType type,
    String title,
    String posterUrl,
    String startAt,
    String endAt,
    String area
) {

    public static UpcomingPerformanceResponse from(
        final UpcomingPerformanceDTO upcomingPerformanceDTO) {
        return new UpcomingPerformanceResponse(
            upcomingPerformanceDTO.typeId(),
            upcomingPerformanceDTO.type(),
            upcomingPerformanceDTO.title(),
            upcomingPerformanceDTO.posterUrl(),
            DateConvertor.convertToDefaultFormat(upcomingPerformanceDTO.startAt()),
            DateConvertor.convertToDefaultFormat(upcomingPerformanceDTO.endAt()),
            upcomingPerformanceDTO.area()
        );
    }
}
