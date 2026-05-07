package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import lombok.Builder;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.application.dto.ConcertFileInfo;
import org.sopt.confeti.domain.concert_reservation_schedule.ConcertReservationScheduleInfo;
import org.sopt.confeti.domain.concert_reservation_url.ConcertReservationFileInfo;
import org.sopt.confeti.domain.concert_reservation_url.ConcertReservationUrl;
import org.sopt.confeti.global.annotation.RedisSerializable;

@RedisSerializable
@Builder
public record ConcertDetailDTO(
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
    List<ConcertReservationDTO> reservations,
    List<ReservationScheduleDTO> reservationSchedules,
    List<ConcertArtistDTO> artists
) {

    public static ConcertDetailDTO toDTO(Concert concert, ConcertFileInfo fileInfo,
        List<ConcertReservationFileInfo> reservationFileInfos) {
        List<ConcertReservationUrl> reservationUrls = concert.getReservationUrls();
        List<ConcertReservationDTO> reservationDTOs = IntStream.range(0, reservationUrls.size())
            .mapToObj(index -> ConcertReservationDTO.of(reservationUrls.get(index),
                reservationFileInfos.get(index)))
            .toList();

        return ConcertDetailDTO.builder()
            .concertId(concert.getId())
            .title(concert.getTitle())
            .startAt(concert.getStartAt())
            .endAt(concert.getEndAt())
            .area(concert.getArea())
            .ageRating(concert.getAgeRating())
            .posterUrl(fileInfo.posterUrl())
            .time(concert.getTime())
            .price(concert.getPrice())
            .address(concert.getAddress())
            .reservations(reservationDTOs)
            .reservationSchedules(concert.getReservationSchedules().stream()
                .map(schedule -> ReservationScheduleDTO.from(schedule.toDomain()))
                .toList())
            .artists(concert.getArtists().stream()
                .map(ConcertArtistDTO::of)
                .toList())
            .build();
    }

    public record ReservationScheduleDTO(
        long reservationScheduleId,
        String roundName,
        LocalDateTime reserveAt
    ) {

        public static ReservationScheduleDTO from(ConcertReservationScheduleInfo info) {
            return new ReservationScheduleDTO(info.id(), info.roundName(), info.reserveAt());
        }
    }
}
