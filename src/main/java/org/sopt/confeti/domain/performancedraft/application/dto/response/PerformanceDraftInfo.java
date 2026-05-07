package org.sopt.confeti.domain.performancedraft.application.dto.response;

import java.time.LocalDateTime;
import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftFileInfo;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftType;

public record PerformanceDraftInfo(
    Long id,
    PerformanceDraftType performanceDraftType,
    DraftStatus status,
    String performanceData,
    String posterUrl,
    String logoUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static PerformanceDraftInfo of(PerformanceDraftDto dto, PerformanceDraftFileInfo fileInfo) {
        return new PerformanceDraftInfo(
            dto.id(),
            dto.performanceDraftType(),
            dto.status(),
            dto.performanceData(),
            fileInfo.posterUrl(),
            fileInfo.logoUrl(),
            dto.createdAt(),
            dto.updatedAt()
        );
    }
}
