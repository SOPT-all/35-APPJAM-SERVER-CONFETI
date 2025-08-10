package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.RecommendPerformanceDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record RecommendPerformanceResponse(
        long performanceId,
        String type,
        String title,
        String posterUrl
) {
    public static RecommendPerformanceResponse from(RecommendPerformanceDTO recommendPerformanceDTO) {
        return new RecommendPerformanceResponse(
                recommendPerformanceDTO.id(),
                recommendPerformanceDTO.type().getName(),
                recommendPerformanceDTO.title(),
                recommendPerformanceDTO.posterUrl()
        );
    }
}

