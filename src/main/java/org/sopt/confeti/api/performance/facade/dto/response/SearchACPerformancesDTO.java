package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;

public record SearchACPerformancesDTO(
    List<SearchACPerformanceDTO> performances
) {

    public SearchACPerformancesDTO(List<SearchACPerformanceDTO> performances) {
        this.performances = performances == null ? List.of() : List.copyOf(performances);
    }

    public static SearchACPerformancesDTO from(List<SearchACPerformanceDTO> performances) {
        return new SearchACPerformancesDTO(performances);
    }
}
