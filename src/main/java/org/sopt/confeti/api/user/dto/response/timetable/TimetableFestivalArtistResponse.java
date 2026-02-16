package org.sopt.confeti.api.user.dto.response.timetable;

import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableFestivalArtistDTO;

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

