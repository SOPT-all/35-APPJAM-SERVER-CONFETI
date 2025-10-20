package org.sopt.confeti.api.user.dto.response.onboard;

import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardFavoriteArtistDTO;

public record UserOnboardFavoriteArtistResponse(
    String id,
    String profileUrl,
    String name
) {

    public static UserOnboardFavoriteArtistResponse from(UserOnboardFavoriteArtistDTO artistDTO) {
        return new UserOnboardFavoriteArtistResponse(
            artistDTO.id(),
            artistDTO.profileUrl(),
            artistDTO.name()
        );
    }

}
