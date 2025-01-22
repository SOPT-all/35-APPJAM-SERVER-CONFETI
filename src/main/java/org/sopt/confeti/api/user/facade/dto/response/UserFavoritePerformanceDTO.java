package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.view.performance.PerformanceDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record UserFavoritePerformanceDTO(
        long typeId,
        PerformanceType type,
        String title,
        String posterPath
) {
    public static UserFavoritePerformanceDTO from(final PerformanceDTO performance) {
        return new UserFavoritePerformanceDTO(
                performance.typeId(),
                performance.type(),
                performance.title(),
                performance.posterPath()
        );
    }
}
