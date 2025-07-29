package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;

public record AnalyzePerformanceTypeDTO(
        String processedTerm,
        PerformanceType_DEPRECATED performanceTypeDEPRECATED
) {
    public static AnalyzePerformanceTypeDTO of(String processedTerm, PerformanceType_DEPRECATED performanceTypeDEPRECATED) {
        return new AnalyzePerformanceTypeDTO(
                processedTerm,
                performanceTypeDEPRECATED
        );
    }
}
