package org.sopt.confeti.domain.elastic_search.application.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.elastic_search.PerformanceDocument;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record SearchPerformanceResult(
        long id,
        String title,
        LocalDate startAt,
        LocalDate endAt,
        String posterPath,
        String area,
        PerformanceType type
) {
    public static SearchPerformanceResult from(PerformanceDocument performanceDocument) {
        return new SearchPerformanceResult(
                performanceDocument.id(),
                performanceDocument.title(),
                performanceDocument.startAt(),
                performanceDocument.endAt(),
                performanceDocument.posterPath(),
                performanceDocument.area(),
                performanceDocument.type()
        );
    }
}
