package org.sopt.confeti.api.dummy.dto.concert;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public record CreateConcertArtistRequest(
    @NotBlank String artistId
) {
    public CreateConcertArtistRequest() {
        this(null);
    }
}
