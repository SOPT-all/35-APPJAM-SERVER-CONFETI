package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record UpcomingPerformanceDTO(
        long id,
        PerformanceType type,
        long typeId,
        String title,
        String posterPath
) {
    public static UpcomingPerformanceDTO from(PerformanceDTO performanceDTO) {
        return new UpcomingPerformanceDTO(
                performanceDTO.id(),
                performanceDTO.type(),
                performanceDTO.typeId(),
                performanceDTO.title(),
                performanceDTO.posterPath()
        );
    }
}
