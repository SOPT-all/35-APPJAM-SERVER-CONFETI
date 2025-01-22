package org.sopt.confeti.api.performance.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.global.util.DateConvertor;

public record FestivalDetailInfoResponse(
        long festivalId,
        String posterUrl,
        String posterBgUrl,
        String title,
        String subtitle,
        String startAt,
        String endAt,
        String area,
        String reserveAt,
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
                DateConvertor.convertToLocalDate(festival.festivalStartAt()),
                DateConvertor.convertToLocalDate(festival.festivalEndAt()),
                festival.festivalArea(),
                DateConvertor.convert(festival.reserveAt()),
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
