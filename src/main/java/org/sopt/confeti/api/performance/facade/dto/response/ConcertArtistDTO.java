package org.sopt.confeti.api.performance.facade.dto.response;


import org.sopt.confeti.global.mapper.dto.concert.ConcertArtist;

public record ConcertArtistDTO(
        String artistId,
        String name,
        String profileUrl
) {
    public static ConcertArtistDTO from(ConcertArtist concertArtist) {
        return new ConcertArtistDTO(
                concertArtist.artistId(),
                concertArtist.name(),
                concertArtist.profileUrl()
        );
    }
}
