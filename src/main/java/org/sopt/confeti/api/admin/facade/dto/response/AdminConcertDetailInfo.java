package org.sopt.confeti.api.admin.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert_artist.ConcertArtist;
import org.sopt.confeti.domain.concert_reservation_url.ConcertReservationUrl;

public record AdminConcertDetailInfo(
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
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<ReservationUrlInfo> reservationUrls,
    List<ArtistInfo> artists
) {

    public record ReservationUrlInfo(
        long reservationUrlId,
        String reservationUrl,
        long ticketVendorId
    ) {

        public static ReservationUrlInfo from(ConcertReservationUrl url) {
            return new ReservationUrlInfo(
                url.getId(),
                url.getReservationUrl(),
                url.getTicketVendor().getId()
            );
        }
    }

    public record ArtistInfo(
        String artistId,
        String name,
        String artworkUrl
    ) {

        public static ArtistInfo from(ConcertArtist concertArtist) {
            return new ArtistInfo(
                concertArtist.getArtist().getId(),
                concertArtist.getArtist().getName(),
                concertArtist.getArtist().getArtworkUrl()
            );
        }
    }

    public static AdminConcertDetailInfo from(Concert concert) {
        return new AdminConcertDetailInfo(
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
            concert.getCreatedAt(),
            concert.getUpdatedAt(),
            concert.getReservationUrls().stream()
                .map(ReservationUrlInfo::from)
                .toList(),
            concert.getArtists().stream()
                .map(ArtistInfo::from)
                .toList()
        );
    }
}
