package org.sopt.confeti.api.setlist.facade.dto.response.search;

import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceInfo;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record SearchPerformanceDTO(
    long id,
    PerformanceType type,
    long typeId,
    String title,
    String posterUrl
) {

    public static SearchPerformanceDTO from(PerformanceInfo performance) {
        return new SearchPerformanceDTO(
            performance.id(),
            performance.type(),
            performance.typeId(),
            performance.title(),
            performance.posterUrl()
        );
    }
}
