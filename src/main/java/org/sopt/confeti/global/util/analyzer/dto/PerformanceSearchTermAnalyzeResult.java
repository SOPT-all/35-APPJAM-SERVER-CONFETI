package org.sopt.confeti.global.util.analyzer.dto;

import org.sopt.confeti.global.common.constant.PerformanceType;

public record PerformanceSearchTermAnalyzeResult(
        String processedTerm,
        PerformanceType performanceType
) {
    public static PerformanceSearchTermAnalyzeResult of(String processedTerm, PerformanceType performanceType) {
        return new PerformanceSearchTermAnalyzeResult(
                processedTerm,
                performanceType
        );
    }


    public static PerformanceSearchTermAnalyzeResult empty() {
        return new PerformanceSearchTermAnalyzeResult(
                "",
                PerformanceType.PERFORMANCE
        );
    }
}
