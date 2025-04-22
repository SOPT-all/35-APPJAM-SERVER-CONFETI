package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.IntendedPerformancesDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record IntendedPerformancesResponse(
        int performanceCount,
        List<IntendedPerformanceResponse> performances
) {
    public static IntendedPerformancesResponse of(IntendedPerformancesDTO performancesDTO,
                                                  S3FileHandler s3FileHandler) {
        return new IntendedPerformancesResponse(
                performancesDTO.performances().size(),
                performancesDTO.performances().stream()
                        .map(performanceDTO -> IntendedPerformanceResponse.of(performanceDTO, s3FileHandler))
                        .toList()
        );
    }
}
