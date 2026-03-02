package org.sopt.confeti.api.admin.dto.request;

import java.util.Optional;
import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceType;
import org.sopt.confeti.domain.performancedraft.application.dto.request.PerformanceDraftUpdateDto;
import org.sopt.confeti.global.common.upload.MultipartFileAdapter;
import org.springframework.web.multipart.MultipartFile;

public record UpdatePerformanceDraftRequest(
    PerformanceType performanceType,
    DraftStatus status,
    String performanceData,
    MultipartFile posterImage,
    MultipartFile logoImage
) {
    public PerformanceDraftUpdateDto toUpdateDto(Long id) {
        MultipartFileAdapter posterAdapter = Optional.ofNullable(posterImage)
            .filter(f -> !f.isEmpty())
            .map(MultipartFileAdapter::new)
            .orElse(null);
        MultipartFileAdapter logoAdapter = Optional.ofNullable(logoImage)
            .filter(f -> !f.isEmpty())
            .map(MultipartFileAdapter::new)
            .orElse(null);
        return PerformanceDraftUpdateDto.of(id, performanceType, status, performanceData, posterAdapter, logoAdapter);
    }
}
