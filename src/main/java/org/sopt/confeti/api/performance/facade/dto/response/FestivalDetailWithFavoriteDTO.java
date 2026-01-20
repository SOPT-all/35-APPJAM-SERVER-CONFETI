package org.sopt.confeti.api.performance.facade.dto.response;

public record FestivalDetailWithFavoriteDTO(
    FestivalDetailDTO festivalDetail,
    boolean isFavorite
) {

    public static FestivalDetailWithFavoriteDTO of(FestivalDetailDTO festivalDetail,
        boolean isFavorite) {
        return new FestivalDetailWithFavoriteDTO(festivalDetail, isFavorite);
    }
}
