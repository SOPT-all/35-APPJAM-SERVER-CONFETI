package org.sopt.confeti.api.admin.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftType;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftDto;

public record PerformanceDraftListResponse(
        Long id,
        PerformanceDraftType performanceType,
        DraftStatus status,
        String title,
        String area,
        String startAt
) {
    public static PerformanceDraftListResponse from(PerformanceDraftDto dto, ObjectMapper objectMapper) {
        String title = null;
        String area = null;
        String startAt = null;

        try {
            JsonNode rootNode = objectMapper.readTree(dto.performanceData());
            title = rootNode.hasNonNull("title") ? rootNode.get("title").asText() : null;
            area = rootNode.hasNonNull("area") ? rootNode.get("area").asText() : null;
            startAt = rootNode.hasNonNull("startAt") ? rootNode.get("startAt").asText() : null;
        } catch (JsonProcessingException e) {
            // 파싱 실패 시 기본값 null
        }

        return new PerformanceDraftListResponse(
                dto.id(),
                dto.performanceDraftType(),
                dto.status(),
                title,
                area,
                startAt
        );
    }
}

