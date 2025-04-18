package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.view.performance.Performance;

public record UserFavoritePerformancesAllDTO(
        List<UserFavoritePerformanceAllDTO> performances
) {
    public static UserFavoritePerformancesAllDTO from(final List<Performance> performances) {
        return new UserFavoritePerformancesAllDTO(
                performances.stream()
                        .map(UserFavoritePerformanceAllDTO::from)
                        .toList()
        );
    }
}
