package org.sopt.confeti.api.performance.dto.request;

import java.util.List;

public record CreateFestivalStageRequest(
        String name,
        int orders,
        List<CreateFestivalTimeRequest> festivalTimes
) {
}
