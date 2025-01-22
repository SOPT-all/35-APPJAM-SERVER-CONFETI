package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserTimetableFestivalArtistDTO;

public record UserTimetableFestivalArtistResponse (
        String artistId,
        String artistName
){
    public static UserTimetableFestivalArtistResponse from(UserTimetableFestivalArtistDTO artist) {
        return new UserTimetableFestivalArtistResponse(
                artist.artistId(),
                artist.artistName()
        );
    }
}

