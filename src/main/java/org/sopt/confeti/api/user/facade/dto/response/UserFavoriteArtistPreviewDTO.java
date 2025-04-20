package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.artist_favorite.ArtistFavorite;

public record UserFavoriteArtistPreviewDTO(
        String artistId,
        String name,
        String profileUrl
) {
    public static UserFavoriteArtistPreviewDTO from(final ArtistFavorite artistFavorite) {
        return new UserFavoriteArtistPreviewDTO(
                artistFavorite.getArtist().getId(),
                artistFavorite.getArtist().getName(),
                artistFavorite.getArtist().getProfileUrl()
        );
    }
}


