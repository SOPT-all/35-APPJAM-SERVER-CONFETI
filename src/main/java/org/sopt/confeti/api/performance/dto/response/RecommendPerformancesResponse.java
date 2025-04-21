package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.RecommendPerformancesDTO;
import org.sopt.confeti.global.util.S3FileHandler;
import java.util.List;

public record RecommendPerformancesResponse(
        List<RecommendPerformanceResponse> performances
) {
    public static RecommendPerformancesResponse of(final RecommendPerformancesDTO recommendPerformancesDTO,
                                                   final S3FileHandler s3FileHandler) {
        return new RecommendPerformancesResponse(
                recommendPerformancesDTO.performances().stream()
                        .map(recommendPerformanceDTO -> RecommendPerformanceResponse.of(recommendPerformanceDTO, s3FileHandler))
                        .toList()
        );
    }
}

