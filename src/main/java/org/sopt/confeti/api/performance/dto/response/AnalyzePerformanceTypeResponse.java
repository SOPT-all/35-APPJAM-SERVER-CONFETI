package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.AnalyzePerformanceTypeDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record AnalyzePerformanceTypeResponse(
        String processedTerm,
        PerformanceType performanceType
) {
    public static AnalyzePerformanceTypeResponse from(AnalyzePerformanceTypeDTO analyzePerformanceTypeDTO) {
        return new AnalyzePerformanceTypeResponse(
                analyzePerformanceTypeDTO.processedTerm(),
                analyzePerformanceTypeDTO.performanceType()
        );
    }
}
