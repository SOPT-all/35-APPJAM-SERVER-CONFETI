package org.sopt.confeti.api.search.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceInfo;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record SearchResultPerformanceDTO(
    long id,
    PerformanceType type,
    long typeId,
    String title,
    String posterUrl,
    LocalDate startAt,
    LocalDate endAt,
    String area,
    boolean isFavorite
) {

    public static SearchResultPerformanceDTO of(PerformanceInfo performance,
        boolean performanceFavorite) {
        return new SearchResultPerformanceDTO(
            performance.id(),
            performance.type(),
            performance.typeId(),
            performance.title(),
            performance.posterUrl(),
            performance.startAt(),
            performance.endAt(),
            performance.area(),
            performanceFavorite
        );
    }
}
