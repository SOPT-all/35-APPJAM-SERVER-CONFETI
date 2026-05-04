package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.view.performance.application.dto.response.PerformancePreviewInfo;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record UserFavoritePerformanceDTO(
    long typeId,
    PerformanceType type,
    String title,
    String posterUrl
) {

    public static UserFavoritePerformanceDTO from(final PerformancePreviewInfo performance) {
        return new UserFavoritePerformanceDTO(
            performance.typeId(),
            performance.type(),
            performance.title(),
            performance.posterUrl()
        );
    }
}
