package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import java.util.stream.IntStream;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoritePerformancesDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record UserFavoritePerformancesResponse(
        List<UserFavoritePerformanceResponse> performances
) {
    public static UserFavoritePerformancesResponse of(final UserFavoritePerformancesDTO performancesDTO, final
                                                      S3FileHandler s3FileHandler) {
        return new UserFavoritePerformancesResponse(
                IntStream.range(0, performancesDTO.performances().size())
                        .mapToObj(i -> UserFavoritePerformanceResponse.of(performancesDTO.performances().get(i), s3FileHandler, i))
                        .toList()
        );
    }
}
