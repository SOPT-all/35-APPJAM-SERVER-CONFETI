package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.global.annotation.RedisSerializable;

@RedisSerializable
public record ConcertDetailDTO(
    long concertId,
    String title,
    LocalDate startAt,
    LocalDate endAt,
    String area,
    String posterPath,
    LocalDateTime reserveAt,
    String ageRating,
    String time,
    String price,
    String address,
    List<ConcertReservationDTO> reservations,
    List<ConcertArtistDTO> artists
) {

    public static ConcertDetailDTO from(Concert concert) {
        return new ConcertDetailDTO(
            concert.getId(),
            concert.getTitle(),
            concert.getStartAt(),
            concert.getEndAt(),
            concert.getArea(),
            concert.getPosterPath(),
            concert.getReserveAt(),
            concert.getAgeRating(),
            concert.getTime(),
            concert.getPrice(),
            concert.getAddress(),
            concert.getReservationUrls().stream()
                .map(ConcertReservationDTO::from)
                .toList(),
            concert.getArtists().stream()
                .map(ConcertArtistDTO::of)
                .toList()
        );
    }
}
