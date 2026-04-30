package org.sopt.confeti.api.admin.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalDetailInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalDetailInfo.DateInfo;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;

public record AdminFestivalDetailResponse(
    long festivalId,
    String title,
    LocalDate startAt,
    LocalDate endAt,
    String area,
    String posterUrl,
    String logoUrl,
    String ageRating,
    String time,
    String price,
    String address,
    TimetableSupportStatus timetableSupportStatus,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<DateResponse> dates,
    List<ReservationUrlResponse> reservationUrls,
    List<ReservationScheduleResponse> reservationSchedules
) {

    public record DateResponse(
        long festivalDateId,
        LocalDate festivalAt,
        LocalTime openAt,
        List<ArtistResponse> artists,
        List<StageResponse> stages
    ) {

        public static DateResponse from(AdminFestivalDetailInfo.DateInfo info) {
            return new DateResponse(
                info.festivalDateId(),
                info.festivalAt(),
                info.openAt(),
                info.artists().stream()
                    .map(ArtistResponse::from)
                    .toList(),
                info.stages().stream()
                    .map(StageResponse::from)
                    .toList()
            );
        }
    }

    public record StageResponse(
        long festivalStageId,
        String name,
        int order,
        List<TimeResponse> times
    ) {

        public static StageResponse from(AdminFestivalDetailInfo.StageInfo info) {
            return new StageResponse(
                info.festivalStageId(),
                info.name(),
                info.order(),
                info.times().stream()
                    .map(TimeResponse::from)
                    .toList()
            );
        }
    }

    public record TimeResponse(
        long festivalTimeId,
        LocalTime startAt,
        LocalTime endAt,
        List<ArtistResponse> artists
    ) {

        public static TimeResponse from(AdminFestivalDetailInfo.TimeInfo info) {
            return new TimeResponse(
                info.festivalTimeId(),
                info.startAt(),
                info.endAt(),
                info.artists().stream()
                    .map(ArtistResponse::from)
                    .toList()
            );
        }
    }

    public record ReservationUrlResponse(
        long reservationUrlId,
        String reservationUrl,
        long ticketVendorId
    ) {

        public static ReservationUrlResponse from(AdminFestivalDetailInfo.ReservationUrlInfo info) {
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

        public static ReservationScheduleResponse from(AdminFestivalDetailInfo.ReservationScheduleInfo info) {
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

        public static ArtistResponse from(AdminFestivalDetailInfo.ArtistInfo info) {
            return new ArtistResponse(
                info.artistId(),
                info.name(),
                info.artworkUrl()
            );
        }
    }

    public static AdminFestivalDetailResponse from(AdminFestivalDetailInfo info) {
        return new AdminFestivalDetailResponse(
            info.festivalId(),
            info.title(),
            info.startAt(),
            info.endAt(),
            info.area(),
            info.posterUrl(),
            info.logoUrl(),
            info.ageRating(),
            info.time(),
            info.price(),
            info.address(),
            info.timetableSupportStatus(),
            info.createdAt(),
            info.updatedAt(),
            info.dates().stream()
                .map(DateResponse::from)
                .toList(),
            info.reservationUrls().stream()
                .map(ReservationUrlResponse::from)
                .toList(),
            info.reservationSchedules().stream()
                .map(ReservationScheduleResponse::from)
                .toList()
        );
    }
}
