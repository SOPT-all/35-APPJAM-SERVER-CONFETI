package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.artist_favorite.ArtistFavorite;

public record UserFavoriteArtistsDTO(List<UserFavoriteArtistDTO> artists) {
    public static UserFavoriteArtistsDTO from(final List<ArtistFavorite> artists) {
        return new UserFavoriteArtistsDTO(
                artists.stream()
                        .map(UserFavoriteArtistDTO::from)
                        .toList()
        );
    }
}