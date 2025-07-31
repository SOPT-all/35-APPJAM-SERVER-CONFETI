package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.global.mapper.dto.concert.Concert;

public record ConcertDetailDTO(
        long concertId,
        String title,
        String subtitle,
        LocalDate startAt,
        LocalDate endAt,
        String area,
        String posterUrl,
        LocalDateTime reserveAt,
        String ageRating,
        String time,
        String price,
        String address,
        boolean isFavorite,
        List<ConcertReservationDTO> reservations,
        List<ConcertArtistDTO> artists
) {
    public static ConcertDetailDTO of(final Concert concert, final boolean isFavorite) {
        return new ConcertDetailDTO(
                concert.performanceId(),
                concert.title(),
                concert.subtitle(),
                concert.startAt(),
                concert.endAt(),
                concert.area(),
                concert.posterUrl(),
                concert.reserveAt(),
                concert.ageRating(),
                concert.time(),
                concert.price(),
                concert.address(),
                isFavorite,
                concert.reservations().stream()
                        .map(ConcertReservationDTO::from)
                        .toList(),
                concert.artists().stream()
                        .map(ConcertArtistDTO::from)
                        .toList()
        );
    }
}
