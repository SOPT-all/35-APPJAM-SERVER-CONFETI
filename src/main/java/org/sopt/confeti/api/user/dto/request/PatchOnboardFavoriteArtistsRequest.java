package org.sopt.confeti.api.user.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import org.sopt.confeti.api.user.facade.dto.request.onboard.PatchOnboardFavoriteArtistsDTO;

public record PatchOnboardFavoriteArtistsRequest(
    @NotNull
    @Valid
    Set<@NotBlank String> deleteFavoriteArtistIds
) {

    public PatchOnboardFavoriteArtistsDTO toDto() {
        return new PatchOnboardFavoriteArtistsDTO(deleteFavoriteArtistIds);
    }
}
