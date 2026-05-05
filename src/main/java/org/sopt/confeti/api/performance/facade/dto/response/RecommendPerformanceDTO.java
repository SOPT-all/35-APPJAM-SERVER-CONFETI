package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceInfo;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record RecommendPerformanceDTO(
    long typeId,
    PerformanceType type,
    String title,
    String posterUrl
) {

    public static RecommendPerformanceDTO from(PerformanceInfo performance) {
        return new RecommendPerformanceDTO(
            performance.typeId(),
            performance.type(),
            performance.title(),
            performance.posterUrl()
        );
    }
}
