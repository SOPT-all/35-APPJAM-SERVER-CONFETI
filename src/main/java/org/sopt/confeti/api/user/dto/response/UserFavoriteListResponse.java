package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserFavoriteArtistListDTO;

public record UserFavoriteListResponse (
        String artistId,
        String name,
        String profileUrl
) {
    public static UserFavoriteListResponse from(final UserFavoriteArtistListDTO artistListDTO) {
        return new UserFavoriteListResponse(
                artistListDTO.artistId(),
                artistListDTO.name(),
                artistListDTO.profileUrl()
        );
    }
}