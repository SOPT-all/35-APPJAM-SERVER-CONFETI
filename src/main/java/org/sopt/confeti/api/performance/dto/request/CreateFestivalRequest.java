package org.sopt.confeti.api.performance.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CreateFestivalRequest(
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
        List<CreateFestivalDateRequest> festivalDates
) {
}
