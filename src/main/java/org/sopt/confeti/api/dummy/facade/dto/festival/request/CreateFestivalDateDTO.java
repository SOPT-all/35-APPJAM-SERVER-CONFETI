package org.sopt.confeti.api.dummy.facade.dto.festival.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.sopt.confeti.api.dummy.dto.festival.CreateFestivalDateRequest;

public record CreateFestivalDateDTO(
        LocalDate festivalAt,
        LocalTime openAt,
        List<CreateFestivalStageDTO> stages
) {
    public static CreateFestivalDateDTO from(CreateFestivalDateRequest request) {
        return new CreateFestivalDateDTO(
                request.festivalAt(),
                request.openAt(),
                request.stages().stream()
                        .map(CreateFestivalStageDTO::from)
                        .toList()
        );
    }
}
