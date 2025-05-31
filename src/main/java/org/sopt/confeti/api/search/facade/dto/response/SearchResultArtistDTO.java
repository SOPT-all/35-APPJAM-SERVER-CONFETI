package org.sopt.confeti.api.search.facade.dto.response;

import java.util.Objects;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record SearchResultArtistDTO(
        String artistId,
        String name,
        String recentAlbumName,
        String profileUrl,
        boolean isFavorite
) {
    public static SearchResultArtistDTO of(ConfetiArtist artist, boolean artistFavorite) {
        String recentAlbumName = null;
        if (Objects.nonNull(artist.getLatestReleaseAlbum())) {
            recentAlbumName = artist.getLatestReleaseAlbum().getName();
        }

        return new SearchResultArtistDTO(
                artist.getId(),
                artist.getName(),
                recentAlbumName,
                artist.getProfileUrl(),
                artistFavorite
        );
    }
}
