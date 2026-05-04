package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoritePerformancesAllDTO;

public record UserFavoritePerformancesAllResponse(
    List<UserFavoritePerformanceAllResponse> performances) {

    public static UserFavoritePerformancesAllResponse from(
        final UserFavoritePerformancesAllDTO performancesDTO) {
        return new UserFavoritePerformancesAllResponse(
            performancesDTO.performances().stream()
                .map(UserFavoritePerformanceAllResponse::from)
                .toList()
        );
    }
}
