package org.sopt.confeti.domain.performancedraft.application.dto.request;

import java.util.Optional;
import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceType;
import org.sopt.confeti.global.common.upload.UploadableFile;

public record PerformanceDraftUpdateDto(
    Long id,
    PerformanceType performanceType,
    DraftStatus status,
    String performanceData,
    UploadableFile posterImage,
    UploadableFile logoImage
) {
    public static PerformanceDraftUpdateDto of(
        Long id, PerformanceType performanceType, DraftStatus status, String performanceData,
        UploadableFile posterImage, UploadableFile logoImage
    ) {
        return new PerformanceDraftUpdateDto(id, performanceType, status, performanceData, posterImage, logoImage);
    }

    public Optional<UploadableFile> getOptionalPosterImage() {
        return Optional.ofNullable(this.posterImage);
    }

    public Optional<UploadableFile> getOptionalLogoImage() {
        return Optional.ofNullable(this.logoImage);
    }
}
