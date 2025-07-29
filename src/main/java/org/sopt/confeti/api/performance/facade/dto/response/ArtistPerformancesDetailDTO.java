package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;

public record ArtistPerformancesDetailDTO(
        long performanceId,
        long typeId,
        PerformanceType_DEPRECATED type,
        String title,
        LocalDate startAt,
        LocalDate endAt,
        String posterPath,
        String area,
        boolean isFavorite
) {
    public static ArtistPerformancesDetailDTO from(PerformanceDTO performance, boolean isFavorite) {
        return new ArtistPerformancesDetailDTO(
                performance.id(),
                performance.typeId(),
                performance.type(),
                performance.title(),
                performance.startAt(),
                performance.endAt(),
                performance.posterPath(),
                performance.area(),
                isFavorite
        );
    }
}
