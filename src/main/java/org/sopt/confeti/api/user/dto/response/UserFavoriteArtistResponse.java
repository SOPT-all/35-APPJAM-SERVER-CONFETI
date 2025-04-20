package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserFavoriteArtistDTO;
import org.sopt.confeti.global.util.DateConvertor;

public record UserFavoriteArtistResponse(
        String artistId,
        String name,
        String profileUrl,
        String createdAt,
        boolean isFavorite
) {
    public static UserFavoriteArtistResponse from(final UserFavoriteArtistDTO favoriteArtistDTO) {
        return new UserFavoriteArtistResponse(
                favoriteArtistDTO.artistId(),
                favoriteArtistDTO.name(),
                favoriteArtistDTO.profilePath(),
                DateConvertor.convertToDefaultFormat(favoriteArtistDTO.createdAt()),
                true
        );
    }
}