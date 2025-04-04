package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record ArtistPerformancesDetailDTO(
        long performanceId,
        long typeId,
        PerformanceType type,
        String title,
        LocalDate startAt,
        LocalDate endAt,
        String posterPath,
        String area,
        boolean isFavorite
) {
    public static ArtistPerformancesDetailDTO from(Performance performance, boolean isFavorite) {
        return new ArtistPerformancesDetailDTO(
                performance.getId(),
                performance.getTypeId(),
                performance.getType(),
                performance.getTitle(),
                performance.getStartAt(),
                performance.getEndAt(),
                performance.getPosterPath(),
                performance.getArea(),
                isFavorite
        );
    }
}
