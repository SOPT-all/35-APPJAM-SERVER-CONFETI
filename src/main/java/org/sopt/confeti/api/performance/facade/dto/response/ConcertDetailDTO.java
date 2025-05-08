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
                concert.getId(),
                concert.getTitle(),
                concert.getSubtitle(),
                concert.getStartAt(),
                concert.getEndAt(),
                concert.getArea(),
                concert.getPosterPath(),
                concert.getReserveAt(),
                concert.getAgeRating(),
                concert.getTime(),
                concert.getPrice(),
                concert.getAddress(),
                isFavorite,
                concert.getReservationUrls().stream()
                        .map(ConcertReservationDTO::from)
                        .toList(),
                concert.getArtists().stream()
                        .map(ConcertArtistDTO::of)
                        .toList()
        );
    }
}
