package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import java.util.stream.IntStream;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;

public record FestivalDetailResponse(
        FestivalDetailInfoResponse festival,
        List<FestivalDetailDateResponse> festivalDates
) {
    private static final int SPACE_BETWEEN_DATE_AND_IDX = 1;
    public static FestivalDetailResponse from(final FestivalDetailDTO festival) {
        return new FestivalDetailResponse(
                FestivalDetailInfoResponse.from(festival),
                IntStream.range(0, festival.dates().size())
                        .mapToObj(idx ->
                                FestivalDetailDateResponse.of(
                                        festival.dates().get(idx), idx + SPACE_BETWEEN_DATE_AND_IDX)
                        )
                        .toList()
        );
    }
}
