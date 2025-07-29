package org.sopt.confeti.api.user.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.view.performance.Performance_DPRECATED;
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
    public static UpcomingPerformanceDTO from(Performance_DPRECATED performanceDPRECATED) {
        return new UpcomingPerformanceDTO(
                performanceDPRECATED.getTypeId(),
                performanceDPRECATED.getType(),
                performanceDPRECATED.getTitle(),
                performanceDPRECATED.getPosterPath(),
                performanceDPRECATED.getStartAt(),
                performanceDPRECATED.getEndAt(),
                performanceDPRECATED.getArea()
        );
    }

}
