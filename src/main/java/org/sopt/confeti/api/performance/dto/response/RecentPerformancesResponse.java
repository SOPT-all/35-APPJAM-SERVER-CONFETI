package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformancesDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record RecentPerformancesResponse(
        List<RecentPerformanceResponse> performances
) {
    public static RecentPerformancesResponse of(final RecentPerformancesDTO recentPerformancesDTO, final S3FileHandler s3FileHandler) {
        return new RecentPerformancesResponse(
                recentPerformancesDTO.performances().stream()
                        .map(recentPerformanceDTO -> RecentPerformanceResponse.of(recentPerformanceDTO, s3FileHandler))
                        .toList()
        );
    }
}
