package org.sopt.confeti.api.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OnboardArtistRequest(
        @NotBlank
        String artistId
) {
}
