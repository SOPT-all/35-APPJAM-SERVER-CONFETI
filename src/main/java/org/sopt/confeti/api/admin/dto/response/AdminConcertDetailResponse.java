package org.sopt.confeti.api.admin.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.api.admin.facade.dto.response.AdminConcertDetailInfo;

public record AdminConcertDetailResponse(
    long concertId,
    String title,
    LocalDate startAt,
    LocalDate endAt,
    String area,
    String posterUrl,
    String ageRating,
    String time,
    String price,
    String address,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<ReservationUrlResponse> reservationUrls,
    List<ReservationScheduleResponse> reservationSchedules,
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

    public record ReservationScheduleResponse(
        long reservationScheduleId,
        String roundName,
        LocalDateTime reserveAt
    ) {

        public static ReservationScheduleResponse from(AdminConcertDetailInfo.ReservationScheduleInfo info) {
            return new ReservationScheduleResponse(
                info.reservationScheduleId(),
                info.roundName(),
                info.reserveAt()
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
            info.startAt(),
            info.endAt(),
            info.area(),
            info.posterUrl(),
            info.ageRating(),
            info.time(),
            info.price(),
            info.address(),
            info.createdAt(),
            info.updatedAt(),
            info.reservationUrls().stream()
                .map(ReservationUrlResponse::from)
                .toList(),
            info.reservationSchedules().stream()
                .map(ReservationScheduleResponse::from)
                .toList(),
            info.artists().stream()
                .map(ArtistResponse::from)
                .toList()
        );
    }
}
