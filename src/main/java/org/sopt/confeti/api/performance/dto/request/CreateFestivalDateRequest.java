package org.sopt.confeti.api.performance.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreateFestivalDateRequest(
        LocalDate festivalAt,
        LocalTime ticketOpenAt,
        List<CreateFestivalStageRequest> festivalStages
) {
}
