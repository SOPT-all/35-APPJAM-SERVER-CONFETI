package org.sopt.confeti.api.dummy.dto.festival;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public record CreateFestivalArtistRequest(
    @NotBlank String artistId
) {
    public CreateFestivalArtistRequest() {
        this(null);
    }
}
