package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserFavoriteArtistPreviewDTO;

public record UserFavoriteArtistPreviewResponse(
        String artistId,
        String name,
        String profileUrl
) {
    public static UserFavoriteArtistPreviewResponse from(final UserFavoriteArtistPreviewDTO artistListDTO) {
        return new UserFavoriteArtistPreviewResponse(
                artistListDTO.artistId(),
                artistListDTO.name(),
                artistListDTO.profileUrl()
        );
    }
}