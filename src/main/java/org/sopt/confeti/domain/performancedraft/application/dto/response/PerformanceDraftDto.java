package org.sopt.confeti.domain.performancedraft.application.dto.response;

import java.time.LocalDateTime;
import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceDraft;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftType;

public record PerformanceDraftDto(
        Long id,
        PerformanceDraftType performanceDraftType,
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
                draft.getPerformanceDraftType(),
                draft.getStatus(),
                draft.getPerformanceData(),
                draft.getPosterPath(),
                draft.getLogoPath(),
                draft.getCreatedAt(),
                draft.getUpdatedAt()
        );
    }
}
