package org.sopt.confeti.api.admin.dto.response;

import org.sopt.confeti.api.admin.facade.dto.response.AdminPerformanceDraftPreviewInfo;
import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftType;

public record PerformanceDraftListResponse(
        Long id,
        PerformanceDraftType performanceType,
        DraftStatus status,
        String title,
        String area,
        String posterUrl,
        String startAt
) {
    public static PerformanceDraftListResponse from(AdminPerformanceDraftPreviewInfo info) {
        return new PerformanceDraftListResponse(
            info.id(),
            info.performanceDraftType(),
            info.status(),
            info.title(),
            info.area(),
            info.posterUrl(),
            info.startAt()
        );
    }
}
