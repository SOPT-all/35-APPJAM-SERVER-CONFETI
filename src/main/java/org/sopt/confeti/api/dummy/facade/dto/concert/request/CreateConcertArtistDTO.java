package org.sopt.confeti.api.dummy.facade.dto.concert.request;

import org.sopt.confeti.api.dummy.dto.concert.CreateConcertArtistRequest;

public record CreateConcertArtistDTO(
        String artistId
) {
    public static CreateConcertArtistDTO from(CreateConcertArtistRequest request) {
        return new CreateConcertArtistDTO(request.getArtistId());
    }
}
