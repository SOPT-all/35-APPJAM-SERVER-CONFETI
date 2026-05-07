package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailWithFavoriteDTO;
import org.sopt.confeti.global.common.constant.ArtistConstant;

public record ConcertDetailResponse(
    ConcertDetailInfoResponse concert,
    boolean isOpen,
    List<ConcertDetailArtistResponse> concertArtists
) {

    public static ConcertDetailResponse from(
        ConcertDetailWithFavoriteDTO concertDetailWithFavorite) {
        List<ConcertDetailArtistResponse> concertArtists = concertDetailWithFavorite.concertDetail()
            .artists().stream()
            .map(ConcertDetailArtistResponse::from)
            .toList();

        return new ConcertDetailResponse(
            ConcertDetailInfoResponse.of(
                concertDetailWithFavorite.concertDetail(),
                concertDetailWithFavorite.isFavorite()),
            concertArtists.size() > ArtistConstant.BOX_OPEN_CRITERIA,
            concertArtists
        );
    }
}
