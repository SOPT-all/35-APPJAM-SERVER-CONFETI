package org.sopt.confeti.api.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceType;
import org.sopt.confeti.domain.performancedraft.application.dto.request.PerformanceDraftCreateDto;
import org.sopt.confeti.global.common.upload.MultipartFileAdapter;
import org.springframework.web.multipart.MultipartFile;

public record CreatePerformanceDraftRequest(
    @NotNull
    PerformanceType performanceType,
    @NotBlank
    String performanceData,
    @NotNull
    MultipartFile posterImage,
    MultipartFile logoImage
) {

    public PerformanceDraftCreateDto toCreateManualDto() {
        MultipartFileAdapter posterAdapter = new MultipartFileAdapter(posterImage);
        MultipartFileAdapter logoAdapter = Optional.ofNullable(logoImage)
            .map(MultipartFileAdapter::new)
            .orElse(null);
        return PerformanceDraftCreateDto.of(
                performanceType, performanceData, DraftStatus.HOLD, posterAdapter, logoAdapter);
    }

}
