package org.sopt.confeti.api.artist.facade.dto.response;

import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record SearchArtistDTO(
        String artistId,
        String name,
        String profileUrl,
        String recentAlbumName,
        boolean isFavorite
) {
    public static SearchArtistDTO from(final ConfetiArtist confetiArtist, final boolean isFavorite) {
        return new SearchArtistDTO(
                confetiArtist.getId(),
                confetiArtist.getName(),
                confetiArtist.getProfileUrl(),
                confetiArtist.getLatestReleaseAlbum().getName(),
                isFavorite
        );
    }
}
