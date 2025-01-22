package org.sopt.confeti.api.performance.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateFestivalArtistRequest(
        @JsonProperty(value = "artist_id")
        String artistId
) {
}
