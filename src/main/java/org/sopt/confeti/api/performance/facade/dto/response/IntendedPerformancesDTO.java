package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;
import java.util.Map;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;

public record IntendedPerformancesDTO(
        List<IntendedPerformanceDTO> performances
) {
    public static IntendedPerformancesDTO of(List<PerformanceDTO> performanceDTOs, Map<Long, Boolean> favorites) {
        return new IntendedPerformancesDTO(
                performanceDTOs.stream()
                        .map(performanceDTO ->
                                IntendedPerformanceDTO.of(performanceDTO, favorites.get(performanceDTO.id()))
                        )
                        .toList()
        );
    }
}
