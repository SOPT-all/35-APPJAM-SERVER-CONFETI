package org.sopt.confeti.api.user.facade.dto.request.onboard;

import java.util.Set;

public record PatchOnboardFavoriteArtistsDTO(
    Set<String> deleteFavoriteArtistIds
) {

}
