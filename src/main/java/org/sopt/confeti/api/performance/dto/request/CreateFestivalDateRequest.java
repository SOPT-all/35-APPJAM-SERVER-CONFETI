package org.sopt.confeti.api.performance.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreateFestivalDateRequest(
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDate festivalAt,
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime ticketOpenAt,
        List<CreateFestivalStageRequest> festivalStages
) {
}
