package org.sopt.confeti.api.admin.dto.response;

import com.fasterxml.jackson.annotation.JsonRawValue;
import java.time.LocalDateTime;
import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftType;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftInfo;

public record PerformanceDraftResponse(
        Long id,
        PerformanceDraftType performanceType,
        DraftStatus status,
        @JsonRawValue
        String performanceData,
        String posterUrl,
        String logoUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PerformanceDraftResponse from(PerformanceDraftInfo info) {
        return new PerformanceDraftResponse(
                info.id(),
                info.performanceDraftType(),
                info.status(),
                info.performanceData(),
                info.posterUrl(),
                info.logoUrl(),
                info.createdAt(),
                info.updatedAt()
        );
    }
}
