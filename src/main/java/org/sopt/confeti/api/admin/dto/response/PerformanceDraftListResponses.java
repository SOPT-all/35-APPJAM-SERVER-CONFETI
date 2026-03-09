package org.sopt.confeti.api.admin.dto.response;

import java.util.List;
import org.sopt.confeti.api.admin.facade.dto.response.AdminPerformanceDraftPreviewInfo;
import org.sopt.confeti.global.util.S3FileHandler;

public record PerformanceDraftListResponses(
        List<PerformanceDraftListResponse> drafts
) {
    public static PerformanceDraftListResponses from(
        List<AdminPerformanceDraftPreviewInfo> infos,
        S3FileHandler s3FileHandler
    ) {
        List<PerformanceDraftListResponse> responses = infos.stream()
                .map(info -> PerformanceDraftListResponse.from(info, s3FileHandler))
                .toList();
        return new PerformanceDraftListResponses(responses);
    }
}
