package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record IntendedPerformanceDTO(
        long id,
        PerformanceType type,
        String title,
        String posterPath,
        LocalDate startAt,
        LocalDate endAt,
        String area,
        boolean isFavorite
) {
    public static IntendedPerformanceDTO of(PerformanceDTO performanceDTO, boolean isFavorite) {
        return new IntendedPerformanceDTO(
                performanceDTO.id(),
                performanceDTO.type(),
                performanceDTO.title(),
                performanceDTO.posterPath(),
                performanceDTO.startAt(),
                performanceDTO.endAt(),
                performanceDTO.area(),
                isFavorite
        );
    }
}
