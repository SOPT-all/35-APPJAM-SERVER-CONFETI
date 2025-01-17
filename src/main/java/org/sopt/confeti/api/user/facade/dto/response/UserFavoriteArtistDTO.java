package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.artistfavorite.ArtistFavorite;

import java.util.List;

public record UserFavoriteArtistDTO (List<UserFavoriteArtistListDTO> artists) {
    public static UserFavoriteArtistDTO from(final List<ArtistFavorite> artists) {
        return new UserFavoriteArtistDTO(
                artists.stream()
                        .map(UserFavoriteArtistListDTO::from)
                        .toList()
        );
    }
}