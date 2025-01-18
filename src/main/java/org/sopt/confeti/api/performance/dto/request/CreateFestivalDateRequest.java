package org.sopt.confeti.api.performance.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public record CreateFestivalDateRequest(
        @DateTimeFormat(pattern = "yyyy.MM.dd")
        LocalDate festivalAt,
        @DateTimeFormat(pattern = "HH:mm:ss")
        LocalTime ticketOpenAt,
        List<CreateFestivalStageRequest> festivalStages
) {
}
