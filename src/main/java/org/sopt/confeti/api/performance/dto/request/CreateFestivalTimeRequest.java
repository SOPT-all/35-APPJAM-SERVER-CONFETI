package org.sopt.confeti.api.performance.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public record CreateFestivalTimeRequest(
        @DateTimeFormat(pattern = "HH:mm:ss")
        LocalTime startAt,
        @DateTimeFormat(pattern = "HH:mm:ss")
        LocalTime endAt,
        List<CreateFestivalArtistRequest> festivalArtists
) {
}
