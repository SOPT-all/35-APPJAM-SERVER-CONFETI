package org.sopt.confeti.api.search.facade.dto.response;

import java.time.LocalDate;

import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.domain.performance.SearchedPerformance;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;
import org.sopt.confeti.global.util.S3FileHandler;

public record SearchResultPerformanceDTO(
        long id,
        PerformanceType type,
        String title,
        String posterUrl,
        LocalDate startAt,
        LocalDate endAt,
        String area,
        boolean isFavorite
) {
    public static SearchResultPerformanceDTO of(SearchedPerformance performance, boolean performanceFavorite) {
        return new SearchResultPerformanceDTO(
                performance.id(),
                performance.type(),
                performance.title(),
                performance.posterUrl(),
                performance.startAt(),
                performance.endAt(),
                performance.area(),
                performanceFavorite
        );
    }
}
