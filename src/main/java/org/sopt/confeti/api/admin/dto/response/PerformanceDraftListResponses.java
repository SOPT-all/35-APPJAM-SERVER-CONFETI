package org.sopt.confeti.api.admin.dto.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftDtos;

import java.util.List;

public record PerformanceDraftListResponses(
        List<PerformanceDraftListResponse> drafts
) {
    public static PerformanceDraftListResponses from(PerformanceDraftDtos dtos, ObjectMapper objectMapper) {
        List<PerformanceDraftListResponse> responses = dtos.drafts().stream()
                .map(dto -> PerformanceDraftListResponse.from(dto, objectMapper))
                .toList();
        return new PerformanceDraftListResponses(responses);
    }
}
