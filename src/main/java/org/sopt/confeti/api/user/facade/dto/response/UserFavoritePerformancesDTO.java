package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformancePreviewInfo;

public record UserFavoritePerformancesDTO(
    List<UserFavoritePerformanceDTO> performances
) {

    public static UserFavoritePerformancesDTO from(
        final List<PerformancePreviewInfo> performances) {
        return new UserFavoritePerformancesDTO(
            performances.stream()
                .map(UserFavoritePerformanceDTO::from)
                .toList()
        );
    }
}
