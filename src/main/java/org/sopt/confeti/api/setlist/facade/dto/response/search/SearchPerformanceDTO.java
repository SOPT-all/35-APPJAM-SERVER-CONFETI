package org.sopt.confeti.api.setlist.facade.dto.response.search;

import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record SearchPerformanceDTO(
        long id,
        PerformanceType type,
        String title,
        String posterPath
) {
    public static SearchPerformanceDTO from(PerformanceDTO performance) {
        return new SearchPerformanceDTO(
                performance.id(),
                performance.type(),
                performance.title(),
                performance.posterPath()
        );
    }
}
