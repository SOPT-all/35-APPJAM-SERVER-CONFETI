package org.sopt.confeti.api.dummy.facade.dto.festival.request;

import org.sopt.confeti.api.dummy.dto.festival.CreateFestivalArtistRequest;

public record CreateFestivalArtistDTO(
        String artistId
) {
    public static CreateFestivalArtistDTO from(CreateFestivalArtistRequest request) {
        return new CreateFestivalArtistDTO(request.getArtistId());
    }
}
