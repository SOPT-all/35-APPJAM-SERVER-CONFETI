package org.sopt.confeti.global.common;

import org.sopt.confeti.global.common.constant.PerformanceType;

public record AnalyzeSearchTermResult(
        String processedTerm,
        PerformanceType performanceType
) {
    public static AnalyzeSearchTermResult of(String processedTerm, PerformanceType performanceType) {
        return new AnalyzeSearchTermResult(
                processedTerm, performanceType
        );
    }

    public static AnalyzeSearchTermResult empty() {
        return new AnalyzeSearchTermResult(
                "",
                PerformanceType.PERFORMANCE
        );
    }
}
