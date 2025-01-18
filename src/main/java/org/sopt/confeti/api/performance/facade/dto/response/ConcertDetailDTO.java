package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.domain.concert.Concert;

public record ConcertDetailDTO(
        long concertId,
        String title,
        String subtitle,
        LocalDate startAt,
        LocalDate endAt,
        String area,
        String posterPath,
        String posterBgPath,
        String infoImgPath,
        String concertReservationBgPath,
        LocalDateTime reserveAt,
        String reservationUrl,
        String reservationOffice,
        String ageRating,
        String time,
        String price,
        List<ConcertArtistDTO> artists
) {
    public static ConcertDetailDTO from(final Concert concert) {
        return new ConcertDetailDTO(
                concert.getId(),
                concert.getConcertTitle(),
                concert.getConcertSubtitle(),
                concert.getConcertStartAt(),
                concert.getConcertEndAt(),
                concert.getConcertArea(),
                concert.getConcertPosterPath(),
                concert.getConcertPosterBgPath(),
                concert.getConcertInfoImgPath(),
                concert.getConcertReservationBgPath(),
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
