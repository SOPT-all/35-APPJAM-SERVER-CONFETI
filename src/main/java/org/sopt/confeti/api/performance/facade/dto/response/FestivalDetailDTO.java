package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;
import org.sopt.confeti.domain.festival_reservation_schedule.FestivalReservationScheduleInfo;
import org.sopt.confeti.global.annotation.RedisSerializable;

@RedisSerializable
public record FestivalDetailDTO(
    long festivalId,
    String title,
    LocalDate startAt,
    LocalDate endAt,
    String area,
    String posterPath,
    String logoPath,
    String ageRating,
    String time,
    String price,
    String address,
    List<FestivalReservationDTO> reservations,
    List<ReservationScheduleDTO> reservationSchedules,
    List<FestivalDetailDateDTO> dates,
    TimetableSupportStatus timetableSupportStatus
) {

    public record ReservationScheduleDTO(
        long reservationScheduleId,
        String roundName,
        LocalDateTime reserveAt
    ) {

        public static ReservationScheduleDTO from(FestivalReservationScheduleInfo info) {
            return new ReservationScheduleDTO(info.id(), info.roundName(), info.reserveAt());
        }
    }

    public static FestivalDetailDTO from(Festival festival) {
        return new FestivalDetailDTO(
            festival.getId(),
            festival.getTitle(),
            festival.getStartAt(),
            festival.getEndAt(),
            festival.getArea(),
            festival.getPosterPath(),
            festival.getLogoPath(),
            festival.getAgeRating(),
            festival.getTime(),
            festival.getPrice(),
            festival.getAddress(),
            festival.getReservationUrls().stream()
                .map(FestivalReservationDTO::from)
                .toList(),
            festival.getReservationSchedules().stream()
                .map(schedule -> ReservationScheduleDTO.from(schedule.toDomain()))
                .toList(),
            festival.getDates().stream()
                .map(FestivalDetailDateDTO::from)
                .toList(),
            festival.getTimetableSupportStatus()
        );
    }
}
