package org.sopt.confeti.api.performance.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalTime;
import java.util.List;

public record CreateFestivalTimeRequest(
        @JsonFormat(pattern = "HH:mm:ss")
        @JsonProperty(value = "start_at")
        LocalTime startAt,
        @JsonFormat(pattern = "HH:mm:ss")
        @JsonProperty(value = "end_at")
        LocalTime endAt,
        @JsonProperty(value = "festival_artists")
        List<CreateFestivalArtistRequest> festivalArtists
) {
}
