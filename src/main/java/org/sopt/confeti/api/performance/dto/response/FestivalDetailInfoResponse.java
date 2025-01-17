package org.sopt.confeti.api.performance.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;

public record FestivalDetailInfoResponse(
        long festivalId,
        String posterUrl,
        String posterBgUrl,
        String title,
        String subtitle,
        LocalDate startAt,
        LocalDate endAt,
        String area,
        LocalDateTime reserveAt,
        String reservationUrl,
        String time,
        String ageRating,
        String reservationOffice,
        String price,
        String infoImgUrl,
        boolean isFavorite
) {
    public static FestivalDetailInfoResponse from(final FestivalDetailDTO festival) {
        return new FestivalDetailInfoResponse(
                festival.festivalId(),
                festival.festivalPosterUrl(),
                festival.festivalPosterBgUrl(),
                festival.festivalTitle(),
                festival.festivalSubtitle(),
                festival.festivalStartAt(),
                festival.festivalEndAt(),
                festival.festivalArea(),
                festival.reserveAt(),
                festival.reservationUrl(),
                festival.time(),
                festival.ageRating(),
                festival.reservationOffice(),
                festival.price(),
                festival.festivalInfoImgUrl(),
                festival.isFavorite()
        );
    }
}
