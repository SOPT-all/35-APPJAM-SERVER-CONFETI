package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.view.performance.PerformanceFavoriteListDTO;
import java.util.List;

public record UserFavoritePerformancesAllDTO(
        List<UserFavoritePerformanceAllDTO> performances
) {
    public static UserFavoritePerformancesAllDTO from(final List<PerformanceFavoriteListDTO> performances) {
        return new UserFavoritePerformancesAllDTO(
                performances.stream()
                        .map(UserFavoritePerformanceAllDTO::from)
                        .toList()
        );
    }
}
