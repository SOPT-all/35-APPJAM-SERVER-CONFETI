package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.concert_artist.ConcertArtist;
import org.sopt.confeti.domain.music.artist.Artist;

public record ConcertArtistDTO(
    String artistId,
    String name,
    String profileUrl
) {

    public static ConcertArtistDTO of(final ConcertArtist concertArtist) {
        Artist artist = concertArtist.getArtist();

        return new ConcertArtistDTO(
            artist.getId(),
            artist.getName(),
            artist.getProfileUrl()
        );
    }
}
