package org.sopt.confeti.api.search.facade.dto.response;

import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record SearchResultArtistDTO(
        String artistId,
        String name,
        String profileUrl,
        boolean isFavorite
) {
    public static SearchResultArtistDTO of(ConfetiArtist artist, boolean artistFavorite) {
        return new SearchResultArtistDTO(
                artist.getId(),
                artist.getName(),
                artist.getProfileUrl(),
                artistFavorite
        );
    }
}
