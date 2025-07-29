package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformancePreviewDTO;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;

public record UserFavoritePerformanceDTO(
        long performanceId,
        PerformanceType type,
        String title,
        String posterPath
) {
    public static UserFavoritePerformanceDTO from(final Performance performance) {
        return new UserFavoritePerformanceDTO(
                performance.getId(),
                performance.getType(),
                performance.getTitle(),
                performance.getPosterPath()
        );
    }
}
