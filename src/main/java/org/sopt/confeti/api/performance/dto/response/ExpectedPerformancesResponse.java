package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.ExpectedPerformancesDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record ExpectedPerformancesResponse(
        List<ExpectedPerformanceResponse> performances
) {
    public static ExpectedPerformancesResponse of(ExpectedPerformancesDTO expectedPerformancesDTO,
                                                  S3FileHandler s3FileHandler) {
        return new ExpectedPerformancesResponse(
                expectedPerformancesDTO.performances().stream()
                        .map(expectedPerformanceDTO -> ExpectedPerformanceResponse.of(expectedPerformanceDTO,
                                s3FileHandler))
                        .toList()
        );
    }
}
