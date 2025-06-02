package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.view.performance.Performance;

import java.util.List;

public record PerformanceIdsDTO(
        List<PerformanceIdDTO> performances
) {
    public static PerformanceIdsDTO from(List<Performance> performances) {
        return new PerformanceIdsDTO(
                performances.stream()
                        .map(PerformanceIdDTO::from)
                        .toList()

        );
    }
}
