package org.sopt.confeti.api.dummy.facade.dto.festival.request;

import java.time.LocalTime;
import java.util.List;
import org.sopt.confeti.api.dummy.dto.festival.CreateFestivalTimeRequest;

public record CreateFestivalTimeDTO(
        LocalTime startAt,
        LocalTime endAt,
        List<CreateFestivalArtistDTO> artists
) {
    public static CreateFestivalTimeDTO from(CreateFestivalTimeRequest request) {
        return new CreateFestivalTimeDTO(
                request.getStartAt(),
                request.getEndAt(),
                request.getArtists().stream()
                        .map(CreateFestivalArtistDTO::from)
                        .toList()
        );
    }
}
