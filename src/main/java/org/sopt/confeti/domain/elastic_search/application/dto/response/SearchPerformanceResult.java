package org.sopt.confeti.domain.elastic_search.application.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.elastic_search.PerformanceDocument;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record SearchPerformanceResult(
        long id,
        PerformanceType type,
        long typeId,
        String title,
        LocalDate startAt,
        LocalDate endAt,
        String posterPath,
        String area
) {
    public static SearchPerformanceResult from(PerformanceDocument performanceDocument) {
        return new SearchPerformanceResult(
                performanceDocument.id(),
                PerformanceType.convert(performanceDocument.type()),
                performanceDocument.typeId(),
                performanceDocument.title(),
                performanceDocument.startAt(),
                performanceDocument.endAt(),
                performanceDocument.posterPath(),
                performanceDocument.area()
        );
    }
}
