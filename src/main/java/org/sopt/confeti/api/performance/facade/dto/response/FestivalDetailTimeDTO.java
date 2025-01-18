package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.sopt.confeti.domain.festivaltime.FestivalTime;

public record FestivalDetailTimeDTO(
        long festivalTimeId,
        LocalTime startAt,
        LocalTime endAt,
        List<FestivalDetailArtistDTO> artists
) {
    public static FestivalDetailTimeDTO from(final FestivalTime festivalTime) {
        return new FestivalDetailTimeDTO(
                festivalTime.getId(),
                festivalTime.getStartAt(),
                festivalTime.getEndAt(),
                festivalTime.getArtists().stream()
                        .map(FestivalDetailArtistDTO::from)
                        .toList()
        );
    }
}
