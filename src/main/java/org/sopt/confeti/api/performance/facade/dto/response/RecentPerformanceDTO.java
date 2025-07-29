package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.view.performance.Performance_DPRECATED;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record RecentPerformanceDTO(
        long performanceId,
        long typeId,
        PerformanceType type,
        String title,
        LocalDate startAt,
        String posterPath
) {
    public static RecentPerformanceDTO of(final Concert concert, final long performanceId) {
        return new RecentPerformanceDTO(
                performanceId,
                concert.getId(),
                PerformanceType.CONCERT,
                concert.getTitle(),
                concert.getStartAt(),
                concert.getPosterPath()
        );
    }

    public static RecentPerformanceDTO of(final Festival festival, final long performanceId) {
        return new RecentPerformanceDTO(
                performanceId,
                festival.getId(),
                PerformanceType.FESTIVAL,
                festival.getTitle(),
                festival.getStartAt(),
                festival.getPosterPath()
        );
    }

    public static RecentPerformanceDTO from(final Performance_DPRECATED performanceDPRECATED) {
        return new RecentPerformanceDTO(
                performanceDPRECATED.getId(),
                performanceDPRECATED.getTypeId(),
                performanceDPRECATED.getType(),
                performanceDPRECATED.getTitle(),
                performanceDPRECATED.getStartAt(),
                performanceDPRECATED.getPosterPath()
        );
    }
}
