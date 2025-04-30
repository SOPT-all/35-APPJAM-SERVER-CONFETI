package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.concert_artist.ConcertArtist;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record ConcertArtistDTO(
        String artistId,
        String name,
        String profileUrl
) {
    public static ConcertArtistDTO of(final ConcertArtist concertArtist) {
        ConfetiArtist confetiArtist = concertArtist.getArtist();

        return new ConcertArtistDTO(
                confetiArtist.getId(),
                confetiArtist.getName(),
                confetiArtist.getProfileUrl()
        );
    }
}
