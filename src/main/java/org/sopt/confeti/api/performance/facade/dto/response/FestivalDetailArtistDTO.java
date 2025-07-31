package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.global.mapper.dto.festival.FestivalArtist;

public record FestivalDetailArtistDTO(
        String artistId,
        String name,
        String profileUrl
) {
    public static FestivalDetailArtistDTO from(FestivalArtist festivalArtist) {
        return new FestivalDetailArtistDTO(
                festivalArtist.artistId(),
                festivalArtist.name(),
                festivalArtist.profileUrl()
        );
    }
}
