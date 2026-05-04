package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceInfo;

public record UpcomingPerformancesDTO(
    List<UpcomingPerformanceDTO> performances
) {

    public static UpcomingPerformancesDTO from(List<PerformanceInfo> performances) {
        return new UpcomingPerformancesDTO(
            performances.stream()
                .map(UpcomingPerformanceDTO::from)
                .toList()
        );
    }
}
