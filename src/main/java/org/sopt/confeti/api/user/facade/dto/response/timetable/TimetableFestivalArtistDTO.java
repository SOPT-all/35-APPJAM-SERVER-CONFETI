package org.sopt.confeti.api.user.facade.dto.response.timetable;

import org.sopt.confeti.domain.festival_artist.FestivalArtist;

public record TimetableFestivalArtistDTO(String artistId, String artistName
) {

    public static TimetableFestivalArtistDTO from(FestivalArtist festivalArtist) {
        return new TimetableFestivalArtistDTO(
            festivalArtist.getArtist().getId(),
            festivalArtist.getArtist().getName()
        );
    }
}
