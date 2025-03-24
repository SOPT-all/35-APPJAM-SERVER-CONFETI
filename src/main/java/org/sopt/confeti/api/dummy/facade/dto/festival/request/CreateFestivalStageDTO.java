package org.sopt.confeti.api.dummy.facade.dto.festival.request;

import java.util.List;
import org.sopt.confeti.api.dummy.dto.festival.CreateFestivalStageRequest;

public record CreateFestivalStageDTO(
        String name,
        int order,
        List<CreateFestivalTimeDTO> times
) {
    public static CreateFestivalStageDTO from(CreateFestivalStageRequest request) {
        return new CreateFestivalStageDTO(
                request.getName(),
                request.getOrder(),
                request.getTimes().stream()
                        .map(CreateFestivalTimeDTO::from)
                        .toList()
        );
    }
}
