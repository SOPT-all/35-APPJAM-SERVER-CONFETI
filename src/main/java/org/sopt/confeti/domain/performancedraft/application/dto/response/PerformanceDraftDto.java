package org.sopt.confeti.domain.performancedraft.application.dto.response;

import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceDraft;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftType;

import java.time.LocalDateTime;

public record PerformanceDraftDto(
        Long id,
        PerformanceDraftType performanceType,
        DraftStatus status,
        String performanceData,
        String posterPath,
        String logoPath,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PerformanceDraftDto from(PerformanceDraft draft) {
        return new PerformanceDraftDto(
                draft.getId(),
                draft.getPerformanceType(),
                draft.getStatus(),
                draft.getPerformanceData(),
                draft.getPosterPath(),
                draft.getLogoPath(),
                draft.getCreatedAt(),
                draft.getUpdatedAt()
        );
    }
}
