package org.sopt.confeti.api.user.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record UpcomingPerformanceDTO(
        long typeId,
        PerformanceType type,
        String title,
        String posterPath,
        LocalDate startAt,
        LocalDate endAt,
        String area
) {
    public static UpcomingPerformanceDTO from(Performance performance) {
        return new UpcomingPerformanceDTO(
                performance.getTypeId(),
                performance.getType(),
                performance.getTitle(),
                performance.getPosterPath(),
                performance.getStartAt(),
                performance.getEndAt(),
                performance.getArea()
        );
    }
}
