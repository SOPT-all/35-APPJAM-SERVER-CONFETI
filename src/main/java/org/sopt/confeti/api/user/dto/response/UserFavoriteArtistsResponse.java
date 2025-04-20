package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoriteArtistDTO;

public record UserFavoriteArtistsResponse(
        int artistCount,
        List<UserFavoriteArtistResponse> artists
) {
    public static UserFavoriteArtistsResponse from(final List<UserFavoriteArtistDTO> favoriteArtistDTO) {
        return new UserFavoriteArtistsResponse(
                favoriteArtistDTO.size(),
                favoriteArtistDTO.stream()
                        .map(UserFavoriteArtistResponse::from)
                        .toList()
        );
    }
}