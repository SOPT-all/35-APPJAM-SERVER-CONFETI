package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.PerformanceIdsDTO;
import java.util.List;

public record PerformanceIdsResponse(
        List<PerformanceIdResponse> performances
) {
    public static PerformanceIdsResponse from(final PerformanceIdsDTO performanceIdsDTO) {
        return new PerformanceIdsResponse(
                performanceIdsDTO.performances().stream()
                        .map(PerformanceIdResponse::from)
                        .toList()
        );
    }
}