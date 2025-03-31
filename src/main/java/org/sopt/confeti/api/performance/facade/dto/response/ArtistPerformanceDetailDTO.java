package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.global.common.constant.PerformanceType;

import java.time.LocalDateTime;

public record ArtistPerformanceDetailDTO(
        long performanceId,
        long typeId,
        PerformanceType type,
        String title,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String posterPath,
        String area,
        boolean isFavorite
) {
    public static ArtistPerformanceDetailDTO from(Performance performance, boolean isFavorite) {
        return new ArtistPerformanceDetailDTO(
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
