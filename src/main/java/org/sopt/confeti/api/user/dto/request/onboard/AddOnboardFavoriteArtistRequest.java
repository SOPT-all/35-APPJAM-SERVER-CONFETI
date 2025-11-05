package org.sopt.confeti.api.user.dto.request.onboard;

import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import org.sopt.confeti.api.user.facade.dto.request.onboard.AddOnboardFavoriteArtistDTO;

public record AddOnboardFavoriteArtistRequest(
    @NotEmpty
    Set<String> artistIds
) {

    public AddOnboardFavoriteArtistDTO toDTO() {
        return new AddOnboardFavoriteArtistDTO(artistIds);
    }
}
