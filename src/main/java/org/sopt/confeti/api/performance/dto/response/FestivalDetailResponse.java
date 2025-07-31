package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import java.util.stream.IntStream;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record FestivalDetailResponse(
        FestivalDetailInfoResponse festival,
        List<FestivalDetailDateResponse> festivalDates
) {
    public static FestivalDetailResponse from(FestivalDetailDTO festival) {
        return new FestivalDetailResponse(
                FestivalDetailInfoResponse.from(festival),
                IntStream.range(0, festival.dates().size())
                        .mapToObj(idx ->
                                FestivalDetailDateResponse.of(
                                        festival.dates().get(idx), idx + 1)
                        )
                        .toList()
        );
    }
}
