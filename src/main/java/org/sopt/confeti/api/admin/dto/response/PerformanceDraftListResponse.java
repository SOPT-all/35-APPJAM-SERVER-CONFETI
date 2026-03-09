package org.sopt.confeti.api.admin.dto.response;

import java.util.Optional;
import org.sopt.confeti.api.admin.facade.dto.response.AdminPerformanceDraftPreviewInfo;
import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftType;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record PerformanceDraftListResponse(
        Long id,
        PerformanceDraftType performanceType,
        DraftStatus status,
        String title,
        String area,
        String posterUrl,
        String startAt
) {
    public static PerformanceDraftListResponse from(
        AdminPerformanceDraftPreviewInfo info,
        S3FileHandler s3FileHandler
    ) {
        String posterUrl = Optional.ofNullable(info.posterPath())
            .map(path -> s3FileHandler.getFileUrl(
                FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.POSTER), path).toString())
            .orElse(null);

        return new PerformanceDraftListResponse(
            info.id(),
            info.performanceDraftType(),
            info.status(),
            info.title(),
            info.area(),
            posterUrl,
            info.startAt()
        );
    }
}
