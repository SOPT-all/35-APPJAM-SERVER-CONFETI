package org.sopt.confeti.api.performance.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreateFestivalDateRequest(
        @JsonFormat(pattern = "yyyy.MM.dd")
        @JsonProperty(value = "festival_at")
        LocalDate festivalAt,
        @JsonFormat(pattern = "HH:mm:ss")
        @JsonProperty(value = "ticket_open_at")
        LocalTime ticketOpenAt,
        @JsonProperty(value = "festival_stages")
        List<CreateFestivalStageRequest> festivalStages
) {
}
