package org.sopt.confeti.domain.view.performance.application.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record PerformanceCursorDTO (
        LocalDateTime performanceStartAt,
        LocalTime artistStartAt
) {
}
