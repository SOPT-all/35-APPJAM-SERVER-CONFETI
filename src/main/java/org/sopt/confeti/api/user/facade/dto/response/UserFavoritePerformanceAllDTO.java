package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.view.performance.PerformanceFavoriteListDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;
import java.time.LocalDate;

public record UserFavoritePerformanceAllDTO(
        long typeId,
        PerformanceType type,
        String title,
        String posterPath,
        LocalDate startAt,
        LocalDate endAt,
        String area
        ) {
    public static UserFavoritePerformanceAllDTO from(PerformanceFavoriteListDTO performance) {
        return new UserFavoritePerformanceAllDTO(
                performance.typeId(),
                performance.type(),
                performance.title(),
                performance.posterPath(),
                performance.startAt(),
                performance.endAt(),
                performance.area()
        );
    }
}

