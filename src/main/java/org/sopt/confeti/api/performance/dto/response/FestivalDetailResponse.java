package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import java.util.stream.IntStream;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailWithFavoriteDTO;

public record FestivalDetailResponse(
    FestivalDetailInfoResponse festival,
    List<FestivalDetailDateResponse> festivalDates
) {

    private static final int SPACE_BETWEEN_DATE_AND_IDX = 1;

    public static FestivalDetailResponse from(FestivalDetailWithFavoriteDTO festivalDetail) {
        return new FestivalDetailResponse(
            FestivalDetailInfoResponse.of(festivalDetail.festivalDetail(),
                festivalDetail.isFavorite()),
            IntStream.range(0, festivalDetail.festivalDetail().dates().size())
                .mapToObj(idx ->
                    FestivalDetailDateResponse.of(
                        festivalDetail.festivalDetail().dates().get(idx),
                        idx + SPACE_BETWEEN_DATE_AND_IDX)
                )
                .toList()
        );
    }
}
