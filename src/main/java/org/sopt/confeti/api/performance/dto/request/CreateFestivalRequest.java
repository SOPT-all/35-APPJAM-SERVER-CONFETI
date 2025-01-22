package org.sopt.confeti.api.performance.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CreateFestivalRequest(
        @JsonProperty(value = "festival_title")
        String festivalTitle,
        @JsonProperty(value = "festival_subtitle")
        String festivalSubtitle,
        @JsonFormat(pattern = "yyyy.MM.dd")
        @JsonProperty(value = "festival_start_at")
        LocalDateTime festivalStartAt,
        @JsonFormat(pattern = "yyyy.MM.dd")
        @JsonProperty(value = "festival_end_at")
        LocalDateTime festivalEndAt,
        @JsonProperty(value = "festival_area")
        String festivalArea,
        @JsonProperty(value = "festival_poster_path")
        String festivalPosterPath,
        @JsonProperty(value = "festival_poster_bg_path")
        String festivalPosterBgPath,
        @JsonProperty(value = "festival_info_img_path")
        String festivalInfoImgPath,
        @JsonProperty(value = "festival_reservation_bg_path")
        String festivalReservationBgPath,
        @JsonProperty(value = "festival_logo_path")
        String festivalLogoPath,
        @JsonFormat(pattern = "yyyy.MM.dd")
        @JsonProperty(value = "reserve_at")
        LocalDateTime reserveAt,
        @JsonProperty(value = "reservation_url")
        String reservationUrl,
        @JsonProperty(value = "reservation_office")
        String reservationOffice,
        @JsonProperty(value = "age_rating")
        String ageRating,
        String time,
        String price,
        @JsonProperty(value = "festival_dates")
        List<CreateFestivalDateRequest> festivalDates
) {
}
