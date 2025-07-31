package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.global.common.constant.ArtistConstant;
import org.sopt.confeti.global.util.S3FileHandler;

public record ConcertDetailResponse(
        ConcertDetailInfoResponse concert,
        boolean isOpen,
        List<ConcertDetailArtistResponse> concertArtists
) {
    public static ConcertDetailResponse from(ConcertDetailDTO concertDetailDTO) {
        List<ConcertDetailArtistResponse> concertArtists = concertDetailDTO.artists().stream()
                .map(ConcertDetailArtistResponse::from)
                .toList();

        return new ConcertDetailResponse(
                ConcertDetailInfoResponse.from(concertDetailDTO),
                concertArtists.size() > ArtistConstant.BOX_OPEN_CRITERIA,
                concertArtists
        );
    }
}
