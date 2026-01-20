package org.sopt.confeti.api.performance.facade.dto.response;

public record ConcertDetailWithFavoriteDTO(
    ConcertDetailDTO concertDetail,
    boolean isFavorite
) {

    public static ConcertDetailWithFavoriteDTO of(ConcertDetailDTO concertDetail,
        boolean isFavorite) {
        return new ConcertDetailWithFavoriteDTO(
            concertDetail,
            isFavorite
        );
    }
}
