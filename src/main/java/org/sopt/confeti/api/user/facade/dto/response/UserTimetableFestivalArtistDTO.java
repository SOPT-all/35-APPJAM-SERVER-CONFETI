package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.festivalartist.FestivalArtist;

public record UserTimetableFestivalArtistDTO (String artistId, String artistName
){
    public static UserTimetableFestivalArtistDTO from(FestivalArtist festivalArtist) {
        return new UserTimetableFestivalArtistDTO(
                festivalArtist.getArtist().getArtistId(),
                festivalArtist.getArtist().getName()
        );
    }
}