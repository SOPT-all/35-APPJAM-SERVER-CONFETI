package org.sopt.confeti.api.performance.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;

public record CreateFestivalRequest(
        String festivalTitle,
        String festivalSubtitle,
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDate festivalStartAt,
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDate festivalEndAt,
        String festivalArea,
        String festivalPosterPath,
        String festivalPosterBgPath,
        String festivalInfoImgPath,
        String festivalReservationBgPath,
        String festivalLogoPath,
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDate reserveAt,
        String reservationUrl,
        String reservationOffice,
        String ageRating,
        String time,
        String price,
        List<CreateFestivalDateRequest> festivalDates
) {
}
