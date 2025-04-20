package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.artist_favorite.ArtistFavorite;

public record UserFavoriteArtistsPreviewDTO(List<UserFavoriteArtistPreviewDTO> artists) {
    public static UserFavoriteArtistsPreviewDTO from(final List<ArtistFavorite> artists) {
        return new UserFavoriteArtistsPreviewDTO(
                artists.stream()
                        .map(UserFavoriteArtistPreviewDTO::from)
                        .toList()
        );
    }
}