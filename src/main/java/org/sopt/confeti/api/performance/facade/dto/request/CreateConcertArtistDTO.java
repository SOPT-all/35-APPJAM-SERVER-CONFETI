package org.sopt.confeti.api.performance.facade.dto.request;

import org.sopt.confeti.api.performance.dto.request.CreateConcertArtistRequest;

public record CreateConcertArtistDTO(
        String artistId
) {
    public static CreateConcertArtistDTO from(final CreateConcertArtistRequest createConcertArtistRequest) {
        return new CreateConcertArtistDTO(
                createConcertArtistRequest.artistId()
        );
    }
}
