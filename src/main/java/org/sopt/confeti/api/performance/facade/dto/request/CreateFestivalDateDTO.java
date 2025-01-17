package org.sopt.confeti.api.performance.facade.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.sopt.confeti.api.performance.dto.request.CreateFestivalDateRequest;

public record CreateFestivalDateDTO(
        LocalDate festivalAt,
        LocalTime openAt,
        List<CreateFestivalStageDTO> stages
) {
    public static CreateFestivalDateDTO from(final CreateFestivalDateRequest createFestivalDateRequest) {
        return new CreateFestivalDateDTO(
                createFestivalDateRequest.festivalAt(),
                createFestivalDateRequest.ticketOpenAt(),
                createFestivalDateRequest.festivalStages().stream()
                        .map(CreateFestivalStageDTO::from)
                        .toList()
        );
    }
}
