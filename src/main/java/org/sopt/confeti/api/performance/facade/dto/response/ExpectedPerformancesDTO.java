package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;

public record ExpectedPerformancesDTO(
        List<ExpectedPerformanceDTO> performances
) {
    public static ExpectedPerformancesDTO from(List<PerformanceDTO> performanceDTOs) {
        return new ExpectedPerformancesDTO(
                performanceDTOs.stream()
                        .map(ExpectedPerformanceDTO::from)
                        .toList()
        );
    }
}
