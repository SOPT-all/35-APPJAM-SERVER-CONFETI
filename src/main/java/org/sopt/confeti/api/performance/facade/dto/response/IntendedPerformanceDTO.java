package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;

public record IntendedPerformanceDTO(
        long id,
        PerformanceType_DEPRECATED type,
        long typeId,
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
                performanceDTO.typeId(),
                performanceDTO.title(),
                performanceDTO.posterPath(),
                performanceDTO.startAt(),
                performanceDTO.endAt(),
                performanceDTO.area(),
                isFavorite
        );
    }
}
