package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceInfo;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record UpcomingPerformanceDTO(
    long id,
    PerformanceType type,
    long typeId,
    String title,
    String posterUrl
) {

    public static UpcomingPerformanceDTO from(PerformanceInfo performance) {
        return new UpcomingPerformanceDTO(
            performance.id(),
            performance.type(),
            performance.typeId(),
            performance.title(),
            performance.posterUrl()
        );
    }
}
