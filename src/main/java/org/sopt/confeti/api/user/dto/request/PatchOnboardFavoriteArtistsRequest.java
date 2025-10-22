package org.sopt.confeti.api.user.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import org.sopt.confeti.api.user.facade.dto.request.PatchOnboardFavoriteArtistsDTO;

public record PatchOnboardFavoriteArtistsRequest(
    @NotNull
    Set<String> deleteFavoriteArtistIds
) {

    public PatchOnboardFavoriteArtistsDTO toDto() {
        return new PatchOnboardFavoriteArtistsDTO(deleteFavoriteArtistIds);
    }
}
