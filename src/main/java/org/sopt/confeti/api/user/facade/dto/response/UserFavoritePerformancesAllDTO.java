package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;

public record UserFavoritePerformancesAllDTO(
        List<UserFavoritePerformanceAllDTO> performances
) {
    public static UserFavoritePerformancesAllDTO from(final List<PerformanceDTO> performances) {
        return new UserFavoritePerformancesAllDTO(
                performances.stream()
                        .map(UserFavoritePerformanceAllDTO::from)
                        .toList()
        );
    }
}
