package org.sopt.confeti.api.admin.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.api.admin.facade.dto.response.AdminConcertDetailInfo;

public record AdminConcertDetailResponse(
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
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<ReservationUrlResponse> reservationUrls,
    List<ArtistResponse> artists
) {

    public record ReservationUrlResponse(
        long reservationUrlId,
        String reservationUrl,
        long ticketVendorId
    ) {

        public static ReservationUrlResponse from(AdminConcertDetailInfo.ReservationUrlInfo info) {
            return new ReservationUrlResponse(
                info.reservationUrlId(),
                info.reservationUrl(),
                info.ticketVendorId()
            );
        }
    }

    public record ArtistResponse(
        String artistId,
        String name,
        String artworkUrl
    ) {

        public static ArtistResponse from(AdminConcertDetailInfo.ArtistInfo info) {
            return new ArtistResponse(
                info.artistId(),
                info.name(),
                info.artworkUrl()
            );
        }
    }

    public static AdminConcertDetailResponse from(AdminConcertDetailInfo info) {
        return new AdminConcertDetailResponse(
            info.concertId(),
            info.title(),
            info.subtitle(),
            info.startAt(),
            info.endAt(),
            info.area(),
            info.posterPath(),
            info.reserveAt(),
            info.ageRating(),
            info.time(),
            info.price(),
            info.address(),
            info.createdAt(),
            info.updatedAt(),
            info.reservationUrls().stream()
                .map(ReservationUrlResponse::from)
                .toList(),
            info.artists().stream()
                .map(ArtistResponse::from)
                .toList()
        );
    }
}
