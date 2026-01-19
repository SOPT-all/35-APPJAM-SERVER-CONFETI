package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ConcertDetailWithFavoriteDTO(
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
    public static ConcertDetailWithFavoriteDTO of(ConcertDetailDTO concertDetail, boolean isFavorite) {
        return new ConcertDetailWithFavoriteDTO(
                concertDetail.concertId(),
                concertDetail.title(),
                concertDetail.subtitle(),
                concertDetail.startAt(),
                concertDetail.endAt(),
                concertDetail.area(),
                concertDetail.posterPath(),
                concertDetail.reserveAt(),
                concertDetail.ageRating(),
                concertDetail.time(),
                concertDetail.price(),
                concertDetail.address(),
                isFavorite,
                concertDetail.reservations(),
                concertDetail.artists()
        );
    }
}
