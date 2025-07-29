package org.sopt.confeti.api.search.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;

public record SearchResultPerformanceDTO(
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
    public static SearchResultPerformanceDTO of(PerformanceDTO performance, boolean performanceFavorite) {
        return new SearchResultPerformanceDTO(
                performance.id(),
                performance.type(),
                performance.typeId(),
                performance.title(),
                performance.posterPath(),
                performance.startAt(),
                performance.endAt(),
                performance.area(),
                performanceFavorite
        );
    }
}
