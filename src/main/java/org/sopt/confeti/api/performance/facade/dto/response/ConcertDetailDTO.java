package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.application.dto.ConcertFileInfo;
import org.sopt.confeti.domain.concert_reservation_schedule.ConcertReservationScheduleInfo;
import org.sopt.confeti.global.annotation.RedisSerializable;

@RedisSerializable
@Builder(toBuilder = true)
public record ConcertDetailDTO(
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
    List<ConcertReservationDTO> reservations,
    List<ReservationScheduleDTO> reservationSchedules,
    List<ConcertArtistDTO> artists
) {

    public record ReservationScheduleDTO(
        long reservationScheduleId,
        String roundName,
        LocalDateTime reserveAt
    ) {

        public static ReservationScheduleDTO from(ConcertReservationScheduleInfo info) {
            return new ReservationScheduleDTO(info.id(), info.roundName(), info.reserveAt());
        }
    }

    public static ConcertDetailDTO from(Concert concert) {
        return ConcertDetailDTO.builder()
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
            .reservations(concert.getReservationUrls().stream()
                .map(ConcertReservationDTO::from)
                .toList())
            .reservationSchedules(concert.getReservationSchedules().stream()
                .map(schedule -> ReservationScheduleDTO.from(schedule.toDomain()))
                .toList())
            .artists(concert.getArtists().stream()
                .map(ConcertArtistDTO::of)
                .toList())
            .build();
    }

    public ConcertDetailDTO withFileUrls(ConcertFileInfo fileUrls) {
        return this.toBuilder()
            .posterUrl(fileUrls.posterUrl())
            .build();
    }
}
