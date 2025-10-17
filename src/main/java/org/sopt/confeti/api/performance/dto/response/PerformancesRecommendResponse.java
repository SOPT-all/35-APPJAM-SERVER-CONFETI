package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.PerformancesRecommendDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record PerformancesRecommendResponse(
        List<PerformanceRecommendResponse> performances
) {
    public static PerformancesRecommendResponse of(PerformancesRecommendDTO performances, S3FileHandler s3FileHandler) {
        return new PerformancesRecommendResponse(
                performances.performances().stream()
                        .map(performance -> PerformanceRecommendResponse.of(performance, s3FileHandler))
                        .toList()
        );
    }
}
