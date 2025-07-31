package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalTime;
import java.util.List;
import org.sopt.confeti.global.mapper.dto.festival.FestivalTime;

public record FestivalDetailTimeDTO(
        LocalTime startAt,
        LocalTime endAt,
        List<FestivalDetailArtistDTO> artists
) {
    public static FestivalDetailTimeDTO from(FestivalTime festivalTime) {
        return new FestivalDetailTimeDTO(
                festivalTime.startAt(),
                festivalTime.endAt(),
                festivalTime.artists().stream()
                        .map(FestivalDetailArtistDTO::from)
                        .toList()
        );
    }
}
