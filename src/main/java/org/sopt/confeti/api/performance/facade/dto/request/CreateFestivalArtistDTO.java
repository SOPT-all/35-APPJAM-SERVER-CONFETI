package org.sopt.confeti.api.performance.facade.dto.request;

import org.sopt.confeti.api.performance.dto.request.CreateFestivalArtistRequest;

public record CreateFestivalArtistDTO(
        String artistId
) {
    public static CreateFestivalArtistDTO from(final CreateFestivalArtistRequest createFestivalArtistRequest) {
        return new CreateFestivalArtistDTO(
                createFestivalArtistRequest.artistId()
        );
    }
}
