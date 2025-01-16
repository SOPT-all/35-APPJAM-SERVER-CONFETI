package org.sopt.confeti.api.performance.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;

public record ConcertDetailInfoResponse(
        long concertId,
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
        String infoImgUrl
) {
    public static ConcertDetailInfoResponse from(final ConcertDetailDTO concertDetailDTO) {
        return new ConcertDetailInfoResponse(
                concertDetailDTO.concertId(),
                concertDetailDTO.posterUrl(),
                concertDetailDTO.posterBgUrl(),
                concertDetailDTO.title(),
                concertDetailDTO.subtitle(),
                concertDetailDTO.startAt(),
                concertDetailDTO.endAt(),
                concertDetailDTO.area(),
                concertDetailDTO.reserveAt(),
                concertDetailDTO.reservationUrl(),
                concertDetailDTO.time(),
                concertDetailDTO.ageRating(),
                concertDetailDTO.reservationOffice(),
                concertDetailDTO.price(),
                concertDetailDTO.infoImgUrl()
        );
    }
}
