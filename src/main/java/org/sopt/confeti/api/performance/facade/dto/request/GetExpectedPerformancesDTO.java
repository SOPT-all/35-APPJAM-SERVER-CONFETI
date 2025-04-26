package org.sopt.confeti.api.performance.facade.dto.request;

import java.util.List;
import org.sopt.confeti.api.performance.dto.request.GetExpectedPerformanceRequest;

public record GetExpectedPerformancesDTO(
        List<GetExpectedPerformanceDTO> expectedPerformanceDTOs
) {
    public static GetExpectedPerformancesDTO from(List<GetExpectedPerformanceRequest> expectedPerformanceRequests) {
        return new GetExpectedPerformancesDTO(
                expectedPerformanceRequests.stream()
                        .map(GetExpectedPerformanceDTO::from)
                        .toList()
        );
    }
}
