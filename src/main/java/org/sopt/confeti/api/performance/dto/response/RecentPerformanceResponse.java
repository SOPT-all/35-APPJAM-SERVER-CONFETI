package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformanceDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.util.DateConvertor;

public record RecentPerformanceResponse(
    long performanceId,
    long typeId,
    PerformanceType type,
    String title,
    String area,
    String startAt,
    String posterUrl,
    boolean isFavorite
) {

    public static RecentPerformanceResponse from(RecentPerformanceDTO recentPerformanceDTO) {
        return new RecentPerformanceResponse(
            recentPerformanceDTO.performanceId(),
            recentPerformanceDTO.typeId(),
            recentPerformanceDTO.type(),
            recentPerformanceDTO.title(),
            recentPerformanceDTO.area(),
            DateConvertor.convertToDefaultFormat(recentPerformanceDTO.startAt()),
            recentPerformanceDTO.posterUrl(),
            recentPerformanceDTO.isFavorite()
        );
    }
}
