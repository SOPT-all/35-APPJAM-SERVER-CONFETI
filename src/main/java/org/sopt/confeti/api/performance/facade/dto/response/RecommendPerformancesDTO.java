package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;

import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.global.util.S3FileHandler;

public record RecommendPerformancesDTO(
        List<RecommendPerformanceDTO> performances
) {
    public static RecommendPerformancesDTO of(List<Performance> performances, S3FileHandler s3FileHandler) {
        return new RecommendPerformancesDTO(
                performances.stream()
                        .map(performance ->  RecommendPerformanceDTO.of(performance, s3FileHandler))
                        .toList()
        );
    }
}