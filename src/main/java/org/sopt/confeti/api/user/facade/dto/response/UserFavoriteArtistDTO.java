package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.artist_favorite.ArtistFavorite;

public record UserFavoriteArtistDTO(List<UserFavoriteArtistListDTO> artists) {
    public static UserFavoriteArtistDTO from(final List<ArtistFavorite> artists) {
        return new UserFavoriteArtistDTO(
                artists.stream()
                        .map(UserFavoriteArtistListDTO::from)
                        .toList()
        );
    }
}