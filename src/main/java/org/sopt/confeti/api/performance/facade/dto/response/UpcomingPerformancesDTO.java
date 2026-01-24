package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;

public record UpcomingPerformancesDTO(
        List<UpcomingPerformanceDTO> performances
) {
    public static UpcomingPerformancesDTO from(List<PerformanceDTO> performanceDTOs) {
        return new UpcomingPerformancesDTO(
                performanceDTOs.stream()
                        .map(UpcomingPerformanceDTO::from)
                        .toList()
        );
    }
}
