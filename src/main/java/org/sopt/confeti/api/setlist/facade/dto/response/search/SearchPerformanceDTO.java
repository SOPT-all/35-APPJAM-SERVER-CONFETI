package org.sopt.confeti.api.setlist.facade.dto.response.search;

import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;

public record SearchPerformanceDTO(
        long id,
        PerformanceType_DEPRECATED type,
        long typeId,
        String title,
        String posterPath
) {
    public static SearchPerformanceDTO from(PerformanceDTO performance) {
        return new SearchPerformanceDTO(
                performance.id(),
                performance.type(),
                performance.typeId(),
                performance.title(),
                performance.posterPath()
        );
    }
}
