package org.sopt.confeti.api.admin.dto.response;

import java.util.List;
import org.sopt.confeti.api.admin.facade.dto.response.AdminPerformanceDraftListInfo;
import org.sopt.confeti.global.util.S3FileHandler;

public record PerformanceDraftListResponses(
        List<PerformanceDraftListResponse> drafts
) {
    public static PerformanceDraftListResponses from(
        AdminPerformanceDraftListInfo info,
        S3FileHandler s3FileHandler
    ) {
        List<PerformanceDraftListResponse> responses = info.drafts().stream()
                .map(previewInfo -> PerformanceDraftListResponse.from(previewInfo, s3FileHandler))
                .toList();
        return new PerformanceDraftListResponses(responses);
    }
}
