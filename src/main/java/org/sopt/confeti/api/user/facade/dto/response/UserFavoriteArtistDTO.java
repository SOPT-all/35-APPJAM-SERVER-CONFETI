package org.sopt.confeti.api.user.facade.dto.response;

import java.time.LocalDateTime;
import org.sopt.confeti.domain.artist_favorite.ArtistFavorite;

public record UserFavoriteArtistDTO(
        String artistId,
        String name,
        String profilePath,
        LocalDateTime createdAt
) {
    public static UserFavoriteArtistDTO from(final ArtistFavorite artistFavorite) {
        return new UserFavoriteArtistDTO(
                artistFavorite.getArtist().getId(),
                artistFavorite.getArtist().getName(),
                artistFavorite.getArtist().getProfileUrl(),
                artistFavorite.getCreatedAt()
        );
    }
}