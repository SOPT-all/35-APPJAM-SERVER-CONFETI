package org.sopt.confeti.api.performance.facade.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.api.performance.dto.request.CreateFestivalRequest;

public record CreateFestivalDTO(
        String festivalTitle,
        String festivalSubtitle,
        LocalDate festivalStartAt,
        LocalDate festivalEndAt,
        String festivalArea,
        String festivalPosterPath,
        String festivalPosterBgPath,
        String festivalInfoImgPath,
        String festivalReservationBgPath,
        String festivalLogoPath,
        LocalDate reserveAt,
        String reservationUrl,
        String reservationOffice,
        String ageRating,
        String time,
        String price,
        List<CreateFestivalDateDTO> dates
) {
    public static CreateFestivalDTO from(final CreateFestivalRequest createFestivalRequest) {
        return new CreateFestivalDTO(
                createFestivalRequest.festivalTitle(),
                createFestivalRequest.festivalSubtitle(),
                createFestivalRequest.festivalStartAt(),
                createFestivalRequest.festivalEndAt(),
                createFestivalRequest.festivalArea(),
                createFestivalRequest.festivalPosterPath(),
                createFestivalRequest.festivalPosterBgPath(),
                createFestivalRequest.festivalInfoImgPath(),
                createFestivalRequest.festivalReservationBgPath(),
                createFestivalRequest.festivalLogoPath(),
                createFestivalRequest.reserveAt(),
                createFestivalRequest.reservationUrl(),
                createFestivalRequest.reservationOffice(),
                createFestivalRequest.ageRating(),
                createFestivalRequest.time(),
                createFestivalRequest.price(),
                createFestivalRequest.festivalDates().stream()
                        .map(CreateFestivalDateDTO::from)
                        .toList()
        );
    }
}
