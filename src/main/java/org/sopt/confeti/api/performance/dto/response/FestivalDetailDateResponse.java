package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDateDTO;

public record FestivalDetailDateResponse(
        long festivalDateId,
        String festivalAt,
        boolean isOpen,
        List<FestivalDetailArtistResponse> artists
) {
    private static final int OPEN_CRITERIA = 4;
    private static final String FESTIVAL_AT_PREFIX = "Day ";

    public static FestivalDetailDateResponse of(final FestivalDetailDateDTO festivalDate, final int order) {
        List<FestivalDetailArtistResponse> artists = festivalDate.stages().stream()
                .flatMap(date -> date.times().stream())
                .flatMap(time -> time.artists().stream())
                .map(FestivalDetailArtistResponse::from)
                .toList();

        return new FestivalDetailDateResponse(
                festivalDate.festivalDateId(),
                FESTIVAL_AT_PREFIX + order,
                artists.size() > OPEN_CRITERIA,
                artists
        );
    }
}
