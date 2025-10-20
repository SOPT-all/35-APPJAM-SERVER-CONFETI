package org.sopt.confeti.api.user.dto.response.onboard;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardFavoriteArtistsDTO;

public record UserOnboardFavoriteArtistsResponse(
    List<UserOnboardFavoriteArtistResponse> artists
) {

    public static UserOnboardFavoriteArtistsResponse from(
        UserOnboardFavoriteArtistsDTO artistsDTO) {
        return new UserOnboardFavoriteArtistsResponse(
            artistsDTO.artists().stream()
                .map(UserOnboardFavoriteArtistResponse::from)
                .toList()
        );
    }
}
