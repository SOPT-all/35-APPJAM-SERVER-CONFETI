package org.sopt.confeti.global.util.analyzer.dto;

import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;

public record PerformanceSearchTermAnalyzeResult(
        String processedTerm,
        PerformanceType_DEPRECATED performanceTypeDEPRECATED
) {
    public static PerformanceSearchTermAnalyzeResult of(String processedTerm, PerformanceType_DEPRECATED performanceTypeDEPRECATED) {
        return new PerformanceSearchTermAnalyzeResult(
                processedTerm,
                performanceTypeDEPRECATED
        );
    }


    public static PerformanceSearchTermAnalyzeResult empty() {
        return new PerformanceSearchTermAnalyzeResult(
                "",
                PerformanceType_DEPRECATED.PERFORMANCE
        );
    }
}
