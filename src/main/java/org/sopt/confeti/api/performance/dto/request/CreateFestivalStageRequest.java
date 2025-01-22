package org.sopt.confeti.api.performance.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record CreateFestivalStageRequest(
        String name,
        int orders,
        @JsonProperty(value = "festival_times")
        List<CreateFestivalTimeRequest> festivalTimes
) {
}
