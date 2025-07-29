package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;

public record ExpectedPerformanceDTO(
        long id,
        PerformanceType_DEPRECATED type,
        long typeId,
        String title,
        String posterPath
) {
    public static ExpectedPerformanceDTO from(PerformanceDTO performanceDTO) {
        return new ExpectedPerformanceDTO(
                performanceDTO.id(),
                performanceDTO.type(),
                performanceDTO.typeId(),
                performanceDTO.title(),
                performanceDTO.posterPath()
        );
    }
}
