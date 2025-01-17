package org.sopt.confeti.api.performance.facade.dto.request;

import java.util.List;
import org.sopt.confeti.api.performance.dto.request.CreateFestivalStageRequest;

public record CreateFestivalStageDTO(
        String name,
        int orders,
        List<CreateFestivalTimeDTO> times
) {
    public static CreateFestivalStageDTO from(final CreateFestivalStageRequest createFestivalStageRequest) {
        return new CreateFestivalStageDTO(
                createFestivalStageRequest.name(),
                createFestivalStageRequest.orders(),
                createFestivalStageRequest.festivalTimes().stream()
                        .map(CreateFestivalTimeDTO::from)
                        .toList()
        );
    }
}
