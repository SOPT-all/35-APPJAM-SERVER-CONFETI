package org.sopt.confeti.domain.view.performance;

import org.sopt.confeti.global.common.constant.PerformanceType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PerformanceFavoriteListDTO (
        long typeId,
        PerformanceType type,
        String title,
        String posterPath,
        LocalDate startAt,
        LocalDate endAt,
        String area
){
    public static PerformanceFavoriteListDTO of(
            final long typeId,
            final String type,
            final String title,
            final String posterPath,
            final LocalDate startAt,
            final LocalDate endAt,
            final String area
    ) {
        return new PerformanceFavoriteListDTO(
                typeId,
                PerformanceType.convert(type),
                title,
                posterPath,
                startAt,
                endAt,
                area
        );
    }
}