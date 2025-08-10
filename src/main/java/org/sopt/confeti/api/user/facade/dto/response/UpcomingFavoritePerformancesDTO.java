package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;

import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.global.util.S3FileHandler;

public record UpcomingFavoritePerformancesDTO(
        List<UpcomingFavoritePerformanceDTO> performances
) {
    public static UpcomingFavoritePerformancesDTO of(List<Performance> performances, S3FileHandler s3FileHandler) {
        return new UpcomingFavoritePerformancesDTO(
                performances.stream()
                        .map(performance -> UpcomingFavoritePerformanceDTO.of(performance, s3FileHandler))
                        .toList()
        );
    }
}
