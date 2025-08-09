package org.sopt.confeti.api.user.facade.dto;

import org.sopt.confeti.domain.performance.Performance;

public record PerformanceCursorDTO(
        String title,
        boolean isFavorite
) {
    public static PerformanceCursorDTO of(Performance performance, boolean isFavorite) {
        return new PerformanceCursorDTO(
                performance.getTitle(),
                isFavorite
        );
    }
}
