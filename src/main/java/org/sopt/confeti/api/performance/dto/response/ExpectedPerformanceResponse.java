package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.ExpectedPerformanceDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record ExpectedPerformanceResponse(
        long performanceId,
        String type,
        String title,
        String posterUrl
) {
    public static ExpectedPerformanceResponse from(ExpectedPerformanceDTO expectedPerformanceDTO) {
        return new ExpectedPerformanceResponse(
                expectedPerformanceDTO.id(),
                expectedPerformanceDTO.type().getName(),
                expectedPerformanceDTO.title(),
                expectedPerformanceDTO.posterUrl()
        );
    }
}
