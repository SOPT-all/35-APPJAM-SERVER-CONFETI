package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDateDTO;
import org.sopt.confeti.global.common.constant.ArtistConstant;

public record FestivalDetailDateResponse(
        String festivalAt,
        boolean isOpen,
        List<FestivalDetailArtistResponse> artists
) {
    private static final String FESTIVAL_AT_PREFIX = "DAY ";

    public static FestivalDetailDateResponse of(final FestivalDetailDateDTO festivalDate, final int order) {
        List<FestivalDetailArtistResponse> artists = festivalDate.stages().stream()
                .flatMap(date -> date.times().stream())
                .flatMap(time -> time.artists().stream())
                .map(FestivalDetailArtistResponse::from)
                .toList();

        return new FestivalDetailDateResponse(
                FESTIVAL_AT_PREFIX + order,
                artists.size() > ArtistConstant.BOX_OPEN_CRITERIA,
                artists
        );
    }
}
