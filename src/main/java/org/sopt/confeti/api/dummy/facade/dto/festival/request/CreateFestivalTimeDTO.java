package org.sopt.confeti.api.dummy.facade.dto.festival.request;

import java.time.LocalTime;
import java.util.List;
import org.sopt.confeti.api.dummy.dto.festival.CreateFestivalTimeRequest;

public record CreateFestivalTimeDTO(
    LocalTime startAt,
    LocalTime endAt,
    List<CreateFestivalArtistDTO> artists) {
    public static CreateFestivalTimeDTO from(CreateFestivalTimeRequest request) {
        return new CreateFestivalTimeDTO(
            request.startAt(),
            request.endAt(),
            request.artists().stream()
                .map(CreateFestivalArtistDTO::from)
                .toList()
        );
    }
}
