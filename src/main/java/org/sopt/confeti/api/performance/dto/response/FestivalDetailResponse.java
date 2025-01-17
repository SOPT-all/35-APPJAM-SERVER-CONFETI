package org.sopt.confeti.api.performance.dto.response;

import java.util.List;

public record FestivalDetailResponse(
        FestivalDetailInfoResponse festival,
        boolean isFestivalArtistCntOverFour,
        List<FestivalDetailDateResponse> festivalDates
) {

}
