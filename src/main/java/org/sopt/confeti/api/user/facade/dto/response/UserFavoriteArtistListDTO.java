package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.artistfavorite.ArtistFavorite;

public record UserFavoriteArtistListDTO (
        String artistId,
        String name,
        String profileUrl
){
    public static UserFavoriteArtistListDTO from(final ArtistFavorite artistFavorite){
        return new UserFavoriteArtistListDTO(
                artistFavorite.getArtist().getArtistId(),
                artistFavorite.getArtist().getName(),
                artistFavorite.getArtist().getProfileUrl()
        );
    }
}


