package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record ConcertDetailResponse(
        ConcertDetailInfoResponse concert,
        boolean isOpen,
        List<ConcertDetailArtistResponse> concertArtists
) {
    private static final int OPEN_CRITERIA = 4;

    public static ConcertDetailResponse of(final ConcertDetailDTO concertDetailDTO, final S3FileHandler s3FileHandler) {
        List<ConcertDetailArtistResponse> concertArtists = concertDetailDTO.artists().stream()
                .map(ConcertDetailArtistResponse::from)
                .toList();

        return new ConcertDetailResponse(
                ConcertDetailInfoResponse.of(concertDetailDTO, s3FileHandler),
                concertArtists.size() > OPEN_CRITERIA,
                concertArtists
        );
    }
}
