package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.FestivalFileInfo;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;
import org.sopt.confeti.domain.festival_reservation_schedule.FestivalReservationScheduleInfo;
import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationFileInfo;
import org.sopt.confeti.global.annotation.RedisSerializable;

@Builder(toBuilder = true)
@RedisSerializable
public record FestivalDetailDTO(
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
    List<FestivalReservationDTO> reservations,
    List<ReservationScheduleDTO> reservationSchedules,
    List<FestivalDetailDateDTO> dates,
    TimetableSupportStatus timetableSupportStatus
) {

    public static FestivalDetailDTO from(Festival festival) {
        return FestivalDetailDTO.builder()
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
            .reservations(festival.getReservationUrls().stream()
                .map(FestivalReservationDTO::from)
                .toList())
            .reservationSchedules(festival.getReservationSchedules().stream()
                .map(schedule -> ReservationScheduleDTO.from(schedule.toDomain()))
                .toList())
            .dates(festival.getDates().stream()
                .map(FestivalDetailDateDTO::from)
                .toList())
            .timetableSupportStatus(festival.getTimetableSupportStatus())
            .build();
    }

    public FestivalDetailDTO withFileUrls(FestivalFileInfo festivalFileInfo) {
        Map<Long, FestivalReservationFileInfo> reservationFileInfoMap =
            festivalFileInfo.festivalReservationFileInfoMap();
        List<FestivalReservationDTO> resolvedReservations = this.reservations.stream()
            .filter(r -> reservationFileInfoMap.containsKey(r.reservationId()))
            .map(r -> r.withFileUrls(reservationFileInfoMap.get(r.reservationId())))
            .toList();

        return this.toBuilder()
            .posterUrl(festivalFileInfo.posterUrl())
            .logoUrl(festivalFileInfo.logoUrl())
            .reservations(resolvedReservations)
            .build();
    }

    public record ReservationScheduleDTO(
        long reservationScheduleId,
        String roundName,
        LocalDateTime reserveAt
    ) {

        public static ReservationScheduleDTO from(FestivalReservationScheduleInfo info) {
            return new ReservationScheduleDTO(info.id(), info.roundName(), info.reserveAt());
        }
    }
}
