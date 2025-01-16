package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.global.util.S3FileHandler;

public record ConcertDetailDTO(
        long concertId,
        String title,
        String subtitle,
        LocalDate startAt,
        LocalDate endAt,
        String area,
        String posterUrl,
        String posterBgUrl,
        String infoImgUrl,
        String concertReservationBgUrl,
        LocalDateTime reserveAt,
        String reservationUrl,
        String reservationOffice,
        String ageRating,
        String time,
        String price,
        List<ConcertArtistDTO> artists
) {
    public static ConcertDetailDTO of(final Concert concert, final S3FileHandler s3FileHandler) {
        return new ConcertDetailDTO(
                concert.getId(),
                concert.getConcertTitle(),
                concert.getConcertSubtitle(),
                concert.getConcertStartAt(),
                concert.getConcertEndAt(),
                concert.getConcertArea(),
                s3FileHandler.getFileUrl(concert.getConcertPosterPath()),
                s3FileHandler.getFileUrl(concert.getConcertPosterBgPath()),
                s3FileHandler.getFileUrl(concert.getConcertInfoImgPath()),
                s3FileHandler.getFileUrl(concert.getConcertReservationBgPath()),
                concert.getReserveAt(),
                concert.getReservationUrl(),
                concert.getReservationOffice(),
                concert.getAgeRating(),
                concert.getTime(),
                concert.getPrice(),
                concert.getArtists().stream()
                        .map(ConcertArtistDTO::of)
                        .toList()
        );
    }
}
