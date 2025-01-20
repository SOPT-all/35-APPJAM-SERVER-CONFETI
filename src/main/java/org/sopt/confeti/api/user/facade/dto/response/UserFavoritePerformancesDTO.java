package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.view.performance.PerformanceDTO;

public record UserFavoritePerformancesDTO(
        List<UserFavoritePerformanceDTO> performances
) {
    public static UserFavoritePerformancesDTO from(final List<PerformanceDTO> performances) {
        return new UserFavoritePerformancesDTO(
                performances.stream()
                        .map(UserFavoritePerformanceDTO::from)
                        .toList()
        );
    }
}
