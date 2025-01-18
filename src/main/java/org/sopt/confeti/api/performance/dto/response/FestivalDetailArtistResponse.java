package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailArtistDTO;

public record FestivalDetailArtistResponse(
        String artistId,
        String name,
        String profileUrl
) {
    public static FestivalDetailArtistResponse from(final FestivalDetailArtistDTO artist) {
        return new FestivalDetailArtistResponse(
                artist.artistId(),
                artist.name(),
                artist.profileUrl()
        );
    }
}
