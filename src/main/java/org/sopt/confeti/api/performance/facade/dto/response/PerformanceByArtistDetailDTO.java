package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.global.common.constant.PerformanceType;

import java.time.LocalDateTime;

public record PerformanceByArtistDetailDTO(
        long performanceId,
        long typeId,
        PerformanceType type,
        String title,
        LocalDateTime performanceStartAt,
        LocalDateTime performanceEndAt,
        String posterPath,
        String area,
        boolean isFavorite
) {
    public static PerformanceByArtistDetailDTO from(Performance performance, boolean isFavorite) {
        return new PerformanceByArtistDetailDTO(
                performance.getId(),
                performance.getTypeId(),
                performance.getType(),
                performance.getTitle(),
                performance.getPerformanceStartAt(),
                performance.getPerformanceEndAt(),
                performance.getPosterPath(),
                performance.getArea(),
                isFavorite
        );
    }
}
