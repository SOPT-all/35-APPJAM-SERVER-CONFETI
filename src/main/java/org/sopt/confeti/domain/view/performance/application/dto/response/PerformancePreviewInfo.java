package org.sopt.confeti.domain.view.performance.application.dto.response;

import lombok.Builder;
import org.sopt.confeti.domain.view.performance.PerformanceFileInfo;
import org.sopt.confeti.global.common.constant.PerformanceType;

@Builder
public record PerformancePreviewInfo(
    long typeId,
    PerformanceType type,
    String title,
    String posterUrl
) {

    public static PerformancePreviewInfo of(PerformancePreviewDTO preview,
        PerformanceFileInfo fileInfo) {
        return PerformancePreviewInfo.builder()
            .typeId(preview.typeId())
            .type(preview.type())
            .title(preview.title())
            .posterUrl(fileInfo.posterUrl())
            .build();
    }
}
