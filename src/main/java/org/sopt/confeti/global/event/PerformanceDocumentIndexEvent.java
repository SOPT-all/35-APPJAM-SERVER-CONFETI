package org.sopt.confeti.global.event;

import org.sopt.confeti.domain.view.performance.Performance;

public record PerformanceDocumentIndexEvent(
    Performance performance
) {
    public static PerformanceDocumentIndexEvent from(Performance performance) {
        return new PerformanceDocumentIndexEvent(performance);
    }
}
