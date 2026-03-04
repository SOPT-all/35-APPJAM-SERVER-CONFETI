package org.sopt.confeti.domain.performancedraft.application.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceDraft;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftType;

import java.time.LocalDateTime;

public record PerformanceDraftDto(
        Long id,
        PerformanceDraftType performanceDraftType,
        DraftStatus status,
        String performanceData,
        String posterUrl,
        String logoUrl,
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
