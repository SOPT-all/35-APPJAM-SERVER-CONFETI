package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.global.common.constant.PerformanceType;

public record AnalyzePerformanceTypeDTO(
        String processedTerm,
        PerformanceType performanceType
) {
    public static AnalyzePerformanceTypeDTO of(String processedTerm, PerformanceType performanceType) {
        return new AnalyzePerformanceTypeDTO(
                processedTerm,
                performanceType
        );
    }
}
