package org.sopt.confeti.api.admin.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.api.admin.facade.dto.response.AdminConcertDetailInfo;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

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

    public static AdminConcertDetailResponse of(AdminConcertDetailInfo info, S3FileHandler s3FileHandler) {
        return new AdminConcertDetailResponse(
            info.concertId(),
            info.title(),
            info.startAt(),
            info.endAt(),
            info.area(),
            s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.CONCERT, FolderPath.POSTER), info.posterPath()).toString(),
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
