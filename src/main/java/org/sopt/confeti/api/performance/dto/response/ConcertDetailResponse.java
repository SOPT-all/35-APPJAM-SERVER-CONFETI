package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;

public record ConcertDetailResponse(
        ConcertDetailInfoResponse concert,
        List<ConcertDetailArtistResponse> concertArtists
) {
    public static ConcertDetailResponse from(final ConcertDetailDTO concertDetailDTO) {
        return new ConcertDetailResponse(
                ConcertDetailInfoResponse.from(concertDetailDTO),
                concertDetailDTO.artists().stream()
                        .map(ConcertDetailArtistResponse::from)
                        .toList()
        );
    }
}
