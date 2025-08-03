package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;

import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record ExpectedPerformancesDTO(
        List<ExpectedPerformanceDTO> performances
) {
    public static ExpectedPerformancesDTO of(List<Performance> performances, S3FileHandler s3FileHandler) {
        return new ExpectedPerformancesDTO(
                performances.stream()
                        .map(performance -> ExpectedPerformanceDTO.of(performance, s3FileHandler))
                        .toList()
        );
    }
}
