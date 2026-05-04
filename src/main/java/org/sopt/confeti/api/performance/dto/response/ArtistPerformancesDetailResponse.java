package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformancesDetailDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.util.DateConvertor;

public record ArtistPerformancesDetailResponse(
        long performanceId,
        long typeId,
        PerformanceType type,
        String title,
        String startAt,
        String endAt,
        String posterUrl,
        String area,
        boolean isFavorite
) {
    public static ArtistPerformancesDetailResponse from(ArtistPerformancesDetailDTO performance) {
        return new ArtistPerformancesDetailResponse(
                performance.performanceId(),
                performance.typeId(),
                performance.type(),
                performance.title(),
                DateConvertor.convertToDefaultFormat(performance.startAt()),
                DateConvertor.convertToDefaultFormat(performance.endAt()),
                performance.posterUrl(),
                performance.area(),
                performance.isFavorite()
        );
    }
}
