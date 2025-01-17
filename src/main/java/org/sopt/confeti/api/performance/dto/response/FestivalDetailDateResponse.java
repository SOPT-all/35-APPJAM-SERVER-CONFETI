package org.sopt.confeti.api.performance.dto.response;

import java.util.List;

public record FestivalDetailDateResponse(
        long festivalDateId,
        String festivalAt,
        List<FestivalDetailArtistResponse> artists
) {
}
