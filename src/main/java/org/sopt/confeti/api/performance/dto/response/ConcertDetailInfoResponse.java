package org.sopt.confeti.api.performance.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.global.util.S3FileHandler;

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
    public static ConcertDetailInfoResponse of(final ConcertDetailDTO concertDetailDTO, final S3FileHandler s3FileHandler) {
        return new ConcertDetailInfoResponse(
                concertDetailDTO.concertId(),
                s3FileHandler.getFileUrl(concertDetailDTO.posterPath()),
                s3FileHandler.getFileUrl(concertDetailDTO.posterBgPath()),
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
                s3FileHandler.getFileUrl(concertDetailDTO.infoImgPath())
        );
    }
}
