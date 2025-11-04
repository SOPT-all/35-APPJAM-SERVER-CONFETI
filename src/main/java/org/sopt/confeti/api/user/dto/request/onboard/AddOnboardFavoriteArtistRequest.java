package org.sopt.confeti.api.user.dto.request.onboard;

import jakarta.validation.constraints.NotEmpty;
import org.sopt.confeti.api.user.facade.dto.request.onboard.AddOnboardFavoriteArtistDTO;

public record AddOnboardFavoriteArtistRequest(
    @NotEmpty
    String artistId,
    @NotEmpty
    String name,
    @NotEmpty
    String profileUrl
) {

    public AddOnboardFavoriteArtistDTO toDTO() {
        return new AddOnboardFavoriteArtistDTO(artistId, name, profileUrl);
    }
}
