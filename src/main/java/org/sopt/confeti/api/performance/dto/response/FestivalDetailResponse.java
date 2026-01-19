package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import java.util.stream.IntStream;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailWithFavoriteDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record FestivalDetailResponse(
    FestivalDetailInfoResponse festival,
    List<FestivalDetailDateResponse> festivalDates
) {

    private static final int SPACE_BETWEEN_DATE_AND_IDX = 1;

    public static FestivalDetailResponse of(FestivalDetailWithFavoriteDTO festivalDetail,
        S3FileHandler s3FileHandler) {
        return new FestivalDetailResponse(
            FestivalDetailInfoResponse.of(festivalDetail.festivalDetail(),
                festivalDetail.isFavorite(),
                s3FileHandler),
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
