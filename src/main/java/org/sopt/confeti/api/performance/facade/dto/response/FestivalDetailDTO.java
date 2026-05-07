package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.FestivalFileInfo;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;
import org.sopt.confeti.domain.festival_reservation_schedule.FestivalReservationScheduleInfo;
import org.sopt.confeti.global.annotation.RedisSerializable;

@Builder
@RedisSerializable
public record FestivalDetailDTO(
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
    List<FestivalReservationDTO> reservations,
    List<ReservationScheduleDTO> reservationSchedules,
    List<FestivalDetailDateDTO> dates,
    TimetableSupportStatus timetableSupportStatus
) {

    public static FestivalDetailDTO toDto(Festival festival, FestivalFileInfo fileInfo) {
        return FestivalDetailDTO.builder()
            .festivalId(festival.getId())
            .title(festival.getTitle())
            .startAt(festival.getStartAt())
            .endAt(festival.getEndAt())
            .area(festival.getArea())
            .posterUrl(fileInfo.posterUrl())
            .logoUrl(fileInfo.logoUrl())
            .ageRating(festival.getAgeRating())
            .time(festival.getTime())
            .price(festival.getPrice())
            .address(festival.getAddress())
            .reservations(festival.getReservationUrls().stream()
                .map(reservation -> FestivalReservationDTO.of(
                    reservation,
                    fileInfo.festivalReservationFileInfoMap().get(reservation.getId())))
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
