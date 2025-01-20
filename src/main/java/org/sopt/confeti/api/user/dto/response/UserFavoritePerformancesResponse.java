package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoritePerformancesDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record UserFavoritePerformancesResponse(
        List<UserFavoritePerformanceResponse> performances
) {
    public static UserFavoritePerformancesResponse of(final UserFavoritePerformancesDTO performancesDTO, final
                                                      S3FileHandler s3FileHandler) {
        return new UserFavoritePerformancesResponse(
                performancesDTO.performances().stream()
                        .map(performanceDTO -> UserFavoritePerformanceResponse.of(performanceDTO, s3FileHandler))
                        .toList()
        );
    }
}
