package org.sopt.confeti.domain.performancedraft.application.dto.request;

import java.util.Optional;

import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceDraft;
import org.sopt.confeti.domain.performancedraft.PerformanceType;
import org.sopt.confeti.global.common.upload.UploadableFile;

public record PerformanceDraftCreateDto(
        PerformanceType performanceType,
        String performanceData,
        DraftStatus status,
        UploadableFile posterImage,
        UploadableFile logoImage
) {
    public static PerformanceDraftCreateDto of(
        PerformanceType performanceType, String performanceData, DraftStatus status, 
        UploadableFile posterImage, UploadableFile logoImage
    ) {
        return new PerformanceDraftCreateDto(performanceType, performanceData, status, posterImage, logoImage);
    }

    public PerformanceDraft toEntity(String posterPath, String logoPath) {
        return PerformanceDraft.create(this.performanceType, this.performanceData, this.status, posterPath, logoPath);
    }

    public Optional<UploadableFile> getOptionalLogoImage(){
        return Optional.ofNullable(this.logoImage);
    }
}
