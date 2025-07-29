package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.Performance_DPRECATED;

import java.util.List;

public record PerformanceIdsDTO(
        List<PerformanceIdDTO> performances
) {
    public static PerformanceIdsDTO from(List<Performance_DPRECATED> performanceDPRECATEDS) {
        return new PerformanceIdsDTO(
                performanceDPRECATEDS.stream()
                        .map(PerformanceIdDTO::from)
                        .toList()

        );
    }
}
