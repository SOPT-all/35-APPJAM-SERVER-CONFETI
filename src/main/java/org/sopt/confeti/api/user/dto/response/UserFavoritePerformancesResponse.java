package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import java.util.stream.IntStream;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoritePerformancesDTO;

public record UserFavoritePerformancesResponse(
    List<UserFavoritePerformanceResponse> performances
) {

    public static UserFavoritePerformancesResponse from(
        final UserFavoritePerformancesDTO performancesDTO) {
        return new UserFavoritePerformancesResponse(
            IntStream.range(0, performancesDTO.performances().size())
                .mapToObj(
                    i -> UserFavoritePerformanceResponse.of(performancesDTO.performances().get(i),
                        i))
                .toList()
        );
    }
}
