package org.sopt.confeti.api.performance.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.global.util.DateConvertor;
import org.springframework.format.annotation.DateTimeFormat;

public record CreateFestivalRequest(
        String festivalTitle,
        String festivalSubtitle,
        @DateTimeFormat(pattern = "yyyy.MM.dd")
        LocalDate festivalStartAt,
        @DateTimeFormat(pattern = "yyyy.MM.dd")
        LocalDate festivalEndAt,
        String festivalArea,
        String festivalPosterPath,
        String festivalPosterBgPath,
        String festivalInfoImgPath,
        String festivalReservationBgPath,
        String festivalLogoPath,
        @DateTimeFormat(pattern = "yyyy.MM.dd")
        LocalDate reserveAt,
        String reservationUrl,
        String reservationOffice,
        String ageRating,
        String time,
        String price,
        List<CreateFestivalDateRequest> festivalDates
) {
}
