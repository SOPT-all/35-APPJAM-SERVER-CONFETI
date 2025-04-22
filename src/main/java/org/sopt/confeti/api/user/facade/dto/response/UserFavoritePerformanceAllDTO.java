package org.sopt.confeti.api.user.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record UserFavoritePerformanceAllDTO(
        long typeId,
        PerformanceType type,
        String title,
        String posterPath,
        LocalDate startAt,
        LocalDate endAt,
        String area
) {
    public static UserFavoritePerformanceAllDTO from(PerformanceDTO performance) {
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

