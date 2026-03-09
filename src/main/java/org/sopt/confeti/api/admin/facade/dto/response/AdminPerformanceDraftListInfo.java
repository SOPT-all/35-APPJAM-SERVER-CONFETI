package org.sopt.confeti.api.admin.facade.dto.response;

import java.util.List;

public record AdminPerformanceDraftListInfo(
    List<AdminPerformanceDraftPreviewInfo> drafts
) {
    public static AdminPerformanceDraftListInfo from(
        List<AdminPerformanceDraftPreviewInfo> drafts
    ) {
        return new AdminPerformanceDraftListInfo(drafts);
    }
}
