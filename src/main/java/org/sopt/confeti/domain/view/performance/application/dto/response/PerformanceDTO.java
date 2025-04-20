package org.sopt.confeti.domain.view.performance.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record PerformanceDTO(
        long id,
        long typeId,
        PerformanceType type,
        String area,
        String title,
        String subtitle,
        LocalDate startAt,
        LocalDate endAt,
        String posterPath,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PerformanceDTO from(final Performance performance) {
        return new PerformanceDTO(
                performance.getId(),
                performance.getTypeId(),
                performance.getType(),
                performance.getArea(),
                performance.getTitle(),
                performance.getSubtitle(),
                performance.getStartAt(),
                performance.getEndAt(),
                performance.getPosterPath(),
                performance.getCreatedAt(),
                performance.getUpdatedAt()
        );
    }
}
