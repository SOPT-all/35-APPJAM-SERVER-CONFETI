package org.sopt.confeti.api.performance.facade.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.sopt.confeti.api.performance.dto.request.CreateFestivalTimeRequest;

public record CreateFestivalTimeDTO(
        LocalTime startAt,
        LocalTime endAt,
        List<CreateFestivalArtistDTO> artists
) {
    public static CreateFestivalTimeDTO from(final CreateFestivalTimeRequest createFestivalTimeRequest) {
        return new CreateFestivalTimeDTO(
                createFestivalTimeRequest.startAt(),
                createFestivalTimeRequest.endAt(),
                createFestivalTimeRequest.festivalArtists().stream()
                        .map(CreateFestivalArtistDTO::from)
                        .toList()
        );
    }
}
