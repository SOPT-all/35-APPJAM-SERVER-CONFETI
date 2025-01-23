package org.sopt.confeti.api.performance.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

public record CreateConcertRequest(
        @JsonProperty(value = "concert_title")
        String concertTitle,
        @JsonProperty(value = "concert_subtitle")
        String concertSubtitle,
        @JsonFormat(pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        @JsonProperty(value = "concert_start_at")
        LocalDate concertStartAt,
        @JsonFormat(pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        @JsonProperty(value = "concert_end_at")
        LocalDate concertEndAt,
        @JsonProperty(value = "concert_area")
        String concertArea,
        @JsonProperty(value = "concert_poster_path")
        String concertPosterPath,
        @JsonProperty(value = "concert_poster_bg_path")
        String concertPosterBgPath,
        @JsonProperty(value = "concert_info_img_path")
        String concertInfoImgPath,
        @JsonProperty(value = "concert_reservation_bg_path")
        String concertReservationBgPath,
        @JsonFormat(pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        @JsonProperty(value = "reserve_at")
        LocalDate reserveAt,
        @JsonProperty(value = "reservation_url")
        String reservationUrl,
        @JsonProperty(value = "reservation_office")
        String reservationOffice,
        @JsonProperty(value = "age_rating")
        String ageRating,
        String time,
        String price,
        @JsonProperty(value = "concert_artists")
        List<CreateConcertArtistRequest> concertArtists
) {
}
