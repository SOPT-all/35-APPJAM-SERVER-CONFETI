package org.sopt.confeti.api.admin.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.FestivalFileInfo;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;
import org.sopt.confeti.domain.festival_artist.FestivalArtist;
import org.sopt.confeti.domain.festival_reservation_schedule.FestivalReservationScheduleInfo;
import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationUrl;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.festival_stage.FestivalStage;
import org.sopt.confeti.domain.festival_time.FestivalTime;

@Builder(toBuilder = true)
public record AdminFestivalDetailInfo(
    long festivalId,
    String title,
    LocalDate startAt,
    LocalDate endAt,
    String area,
    String posterPath,
    String posterUrl,
    String logoPath,
    String logoUrl,
    String ageRating,
    String time,
    String price,
    String address,
    TimetableSupportStatus timetableSupportStatus,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<DateInfo> dates,
    List<ReservationUrlInfo> reservationUrls,
    List<ReservationScheduleInfo> reservationSchedules
) {

    public record DateInfo(
        long festivalDateId,
        LocalDate festivalAt,
        LocalTime openAt,
        List<ArtistInfo> artists,
        List<StageInfo> stages
    ) {

        public static DateInfo from(FestivalDate festivalDate) {
            return new DateInfo(
                festivalDate.getId(),
                festivalDate.getFestivalAt(),
                festivalDate.getOpenAt(),
                festivalDate.getArtists().stream()
                    .map(ArtistInfo::from)
                    .toList(),
                festivalDate.getStages().stream()
                    .map(StageInfo::from)
                    .toList()
            );
        }
    }

    public record StageInfo(
        long festivalStageId,
        String name,
        int order,
        List<TimeInfo> times
    ) {

        public static StageInfo from(FestivalStage festivalStage) {
            return new StageInfo(
                festivalStage.getId(),
                festivalStage.getName(),
                festivalStage.getOrder(),
                festivalStage.getTimes().stream()
                    .map(TimeInfo::from)
                    .toList()
            );
        }
    }

    public record TimeInfo(
        long festivalTimeId,
        LocalTime startAt,
        LocalTime endAt,
        List<ArtistInfo> artists
    ) {

        public static TimeInfo from(FestivalTime festivalTime) {
            return new TimeInfo(
                festivalTime.getId(),
                festivalTime.getStartAt(),
                festivalTime.getEndAt(),
                festivalTime.getArtists().stream()
                    .map(ArtistInfo::from)
                    .toList()
            );
        }
    }

    public record ReservationUrlInfo(
        long reservationUrlId,
        String reservationUrl,
        long ticketVendorId
    ) {

        public static ReservationUrlInfo from(FestivalReservationUrl url) {
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

        public static ReservationScheduleInfo from(FestivalReservationScheduleInfo info) {
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

        public static ArtistInfo from(FestivalArtist festivalArtist) {
            return new ArtistInfo(
                festivalArtist.getArtist().getId(),
                festivalArtist.getArtist().getName(),
                festivalArtist.getArtist().getArtworkUrl()
            );
        }
    }

    public static AdminFestivalDetailInfo from(Festival festival) {
        return AdminFestivalDetailInfo.builder()
            .festivalId(festival.getId())
            .title(festival.getTitle())
            .startAt(festival.getStartAt())
            .endAt(festival.getEndAt())
            .area(festival.getArea())
            .posterPath(festival.getPosterPath())
            .logoPath(festival.getLogoPath())
            .ageRating(festival.getAgeRating())
            .time(festival.getTime())
            .price(festival.getPrice())
            .address(festival.getAddress())
            .timetableSupportStatus(festival.getTimetableSupportStatus())
            .createdAt(festival.getCreatedAt())
            .updatedAt(festival.getUpdatedAt())
            .dates(festival.getDates().stream()
                .map(DateInfo::from)
                .toList())
            .reservationUrls(festival.getReservationUrls().stream()
                .map(ReservationUrlInfo::from)
                .toList())
            .reservationSchedules(festival.getReservationSchedules().stream()
                .map(schedule -> ReservationScheduleInfo.from(schedule.toDomain()))
                .toList())
            .build();
    }

    public AdminFestivalDetailInfo withFileUrls(FestivalFileInfo festivalFileInfo) {
        return this.toBuilder()
            .posterUrl(festivalFileInfo.posterUrl())
            .logoUrl(festivalFileInfo.logoUrl())
            .build();
    }
}
