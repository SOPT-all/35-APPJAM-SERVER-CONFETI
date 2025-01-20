package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.global.common.constant.PerformanceType;

public record UserFavoritePerformanceDTO(
        long performanceId,
        PerformanceType type,
        String title,
        String posterPath
) {
}
