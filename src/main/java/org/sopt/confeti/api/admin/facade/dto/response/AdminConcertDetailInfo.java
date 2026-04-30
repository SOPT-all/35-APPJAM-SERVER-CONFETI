package org.sopt.confeti.api.admin.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.application.dto.ConcertFileInfo;
import org.sopt.confeti.domain.concert_artist.ConcertArtist;
import org.sopt.confeti.domain.concert_reservation_schedule.ConcertReservationScheduleInfo;
import org.sopt.confeti.domain.concert_reservation_url.ConcertReservationUrl;

@Builder(toBuilder = true)
public record AdminConcertDetailInfo(
    long concertId,
    String title,
    LocalDate startAt,
    LocalDate endAt,
    String area,
    String posterPath,
    String posterUrl,
    String ageRating,
    String time,
    String price,
    String address,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<ReservationUrlInfo> reservationUrls,
    List<ReservationScheduleInfo> reservationSchedules,
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

    public record ReservationScheduleInfo(
        long reservationScheduleId,
        String roundName,
        LocalDateTime reserveAt
    ) {

        public static ReservationScheduleInfo from(ConcertReservationScheduleInfo info) {
            return new ReservationScheduleInfo(
                info.id(),
                info.roundName(),
                info.reserveAt()
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
        return AdminConcertDetailInfo.builder()
            .concertId(concert.getId())
            .title(concert.getTitle())
            .startAt(concert.getStartAt())
            .endAt(concert.getEndAt())
            .area(concert.getArea())
            .posterPath(concert.getPosterPath())
            .ageRating(concert.getAgeRating())
            .time(concert.getTime())
            .price(concert.getPrice())
            .address(concert.getAddress())
            .createdAt(concert.getCreatedAt())
            .updatedAt(concert.getUpdatedAt())
            .reservationUrls(concert.getReservationUrls().stream()
                .map(ReservationUrlInfo::from)
                .toList())
            .reservationSchedules(concert.getReservationSchedules().stream()
                .map(schedule -> ReservationScheduleInfo.from(schedule.toDomain()))
                .toList())
            .artists(concert.getArtists().stream()
                .map(ArtistInfo::from)
                .toList())
            .build();
    }

    public AdminConcertDetailInfo withFileUrls(ConcertFileInfo fileUrls) {
        return this.toBuilder()
            .posterUrl(fileUrls.posterUrl())
            .build();
    }
}
