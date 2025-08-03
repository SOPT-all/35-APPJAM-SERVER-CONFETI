package org.sopt.confeti.api.search.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.api.search.facade.dto.response.SearchResultPerformanceDTO;
import org.sopt.confeti.domain.performance.PerformanceType;

public record SearchResultPerformanceResponse(
        long performanceId,
        PerformanceType type,
        String title,
        String posterUrl,
        LocalDate startAt,
        LocalDate endAt,
        String area,
        boolean isFavorite
) {
    public static SearchResultPerformanceResponse from(SearchResultPerformanceDTO performance) {
        return new SearchResultPerformanceResponse(
                performance.id(),
                performance.type(),
                performance.title(),
                performance.posterUrl(),
                performance.startAt(),
                performance.endAt(),
                performance.area(),
                performance.isFavorite()
        );
    }
}
