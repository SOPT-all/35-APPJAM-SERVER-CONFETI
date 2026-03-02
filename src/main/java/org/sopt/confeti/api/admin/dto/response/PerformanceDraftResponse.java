package org.sopt.confeti.api.admin.dto.response;

import com.fasterxml.jackson.annotation.JsonRawValue;
import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceType;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftDto;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

import java.time.LocalDateTime;
import java.util.Optional;

public record PerformanceDraftResponse(
        Long id,
        PerformanceType performanceType,
        DraftStatus status,
        @JsonRawValue
        String performanceData,
        String posterPath,
        String logoPath,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PerformanceDraftResponse from(PerformanceDraftDto dto, S3FileHandler s3FileHandler) {
        return new PerformanceDraftResponse(
                dto.id(),
                dto.performanceType(),
                dto.status(),
                dto.performanceData(),
                Optional.ofNullable(dto.posterPath())
                    .map(path -> s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.POSTER), path).toString())
                    .orElse(null),
                Optional.ofNullable(dto.logoPath())
                    .map(path -> s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.LOGO), path).toString())
                    .orElse(null),
                dto.createdAt(),
                dto.updatedAt()
        );
    }
}
