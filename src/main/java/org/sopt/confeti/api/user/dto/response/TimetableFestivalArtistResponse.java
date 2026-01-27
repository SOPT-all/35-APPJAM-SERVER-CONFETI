package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.TimetableFestivalArtistDTO;

public record TimetableFestivalArtistResponse(
        String artistId,
        String artistName
) {
    public static TimetableFestivalArtistResponse from(TimetableFestivalArtistDTO artist) {
        return new TimetableFestivalArtistResponse(
                artist.artistId(),
                artist.artistName()
        );
    }
}

