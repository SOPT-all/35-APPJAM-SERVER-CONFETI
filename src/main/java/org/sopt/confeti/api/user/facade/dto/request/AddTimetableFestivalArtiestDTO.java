package org.sopt.confeti.api.user.facade.dto.request;

import org.sopt.confeti.api.user.dto.request.AddTimetableFestivalArtistRequest;

public record AddTimetableFestivalArtiestDTO(
        long festivalId
) {
    public static AddTimetableFestivalArtiestDTO from(final AddTimetableFestivalArtistRequest addTimetableFestivalArtistRequest) {
        return new AddTimetableFestivalArtiestDTO(
                addTimetableFestivalArtistRequest.festivalId()
        );
    }
}
