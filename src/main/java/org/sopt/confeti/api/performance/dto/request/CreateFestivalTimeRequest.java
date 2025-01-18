package org.sopt.confeti.api.performance.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import java.util.List;

public record CreateFestivalTimeRequest(
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime startAt,
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime endAt,
        List<CreateFestivalArtistRequest> festivalArtists
) {
}
