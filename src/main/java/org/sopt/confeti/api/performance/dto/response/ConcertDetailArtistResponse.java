package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.ConcertArtistDTO;

public record ConcertDetailArtistResponse(
        String artistId,
        String name,
        String profileUrl
) {
    public static ConcertDetailArtistResponse of(final ConcertArtistDTO concertArtistDTO) {
        return new ConcertDetailArtistResponse(
                concertArtistDTO.artistId(),
                concertArtistDTO.name(),
                concertArtistDTO.profileUrl()
        );
    }
}
