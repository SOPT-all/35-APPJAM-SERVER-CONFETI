package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.UpcomingPerformancesDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record UpcomingPerformancesResponse(
        List<UpcomingPerformanceResponse> performances
) {
    public static UpcomingPerformancesResponse of(UpcomingPerformancesDTO upcomingPerformancesDTO,
                                                  S3FileHandler s3FileHandler) {
        return new UpcomingPerformancesResponse(
                upcomingPerformancesDTO.performances().stream()
                        .map(upcomingPerformanceDTO -> UpcomingPerformanceResponse.of(upcomingPerformanceDTO,
                                s3FileHandler))
                        .toList()
        );
    }
}
