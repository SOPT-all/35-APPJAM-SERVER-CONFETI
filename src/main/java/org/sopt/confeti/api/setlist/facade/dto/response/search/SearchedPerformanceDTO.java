package org.sopt.confeti.api.setlist.facade.dto.response.search;

import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.domain.performance.SearchedPerformance;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;

public record SearchedPerformanceDTO(
        long id,
        PerformanceType type,
        String title,
        String posterUrl
) {
    public static SearchedPerformanceDTO from(SearchedPerformance performance) {
        return new SearchedPerformanceDTO(
                performance.id(),
                performance.type(),
                performance.title(),
                performance.posterUrl()
        );
    }
}
