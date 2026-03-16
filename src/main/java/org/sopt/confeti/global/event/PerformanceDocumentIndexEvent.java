package org.sopt.confeti.global.event;

import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;

public record PerformanceDocumentIndexEvent(
    PerformanceDTO performanceDTO
) {
    public static PerformanceDocumentIndexEvent from(Performance performance) {
        return new PerformanceDocumentIndexEvent(PerformanceDTO.from(performance));
    }
}
