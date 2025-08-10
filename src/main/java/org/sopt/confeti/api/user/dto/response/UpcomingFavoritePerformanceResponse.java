package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UpcomingFavoritePerformanceDTO;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.global.util.DateConvertor;

public record UpcomingFavoritePerformanceResponse(
        long performanceId,
        PerformanceType type,
        String title,
        String posterUrl,
        String startAt,
        String endAt,
        String area,
        boolean isFavorite
) {
    public static UpcomingFavoritePerformanceResponse from(UpcomingFavoritePerformanceDTO upcomingFavoritePerformanceDTO) {
        return new UpcomingFavoritePerformanceResponse(
                upcomingFavoritePerformanceDTO.id(),
                upcomingFavoritePerformanceDTO.type(),
                upcomingFavoritePerformanceDTO.title(),
                upcomingFavoritePerformanceDTO.posterUrl(),
                DateConvertor.convertToDefaultFormat(upcomingFavoritePerformanceDTO.startAt()),
                DateConvertor.convertToDefaultFormat(upcomingFavoritePerformanceDTO.endAt()),
                upcomingFavoritePerformanceDTO.area(),
                true
        );
    }
}


