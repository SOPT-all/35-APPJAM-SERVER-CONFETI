package org.sopt.confeti.api.admin.dto.response;

import java.util.List;
import org.sopt.confeti.api.admin.facade.dto.response.AdminPerformanceDraftListInfo;

public record PerformanceDraftListResponses(
        List<PerformanceDraftListResponse> drafts
) {
    public static PerformanceDraftListResponses from(AdminPerformanceDraftListInfo info) {
        List<PerformanceDraftListResponse> responses = info.drafts().stream()
                .map(PerformanceDraftListResponse::from)
                .toList();
        return new PerformanceDraftListResponses(responses);
    }
}
