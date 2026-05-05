package org.sopt.confeti.domain.view.performance.application.dto.response;

import lombok.Builder;
import org.sopt.confeti.global.common.constant.PerformanceType;

@Builder
public record PerformancePreviewDTO(
        long typeId,
        PerformanceType type,
        String title,
        String posterPath
) {
    public static PerformancePreviewDTO of(
            final long typeId,
            final String type,
            final String title,
            final String posterPath
    ) {
        return PerformancePreviewDTO.builder()
                .typeId(typeId)
                .type(PerformanceType.convert(type))
                .title(title)
                .posterPath(posterPath)
                .build();
    }
}
