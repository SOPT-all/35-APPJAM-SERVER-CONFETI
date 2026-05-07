package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.global.util.DateConvertor;

public record ConcertDetailInfoResponse(
    long concertId,
    String posterUrl,
    String title,
    String startAt,
    String endAt,
    String area,
    String time,
    String ageRating,
    String price,
    String address,
    boolean isFavorite,
    List<ConcertReservationResponse> reservations,
    List<ReservationScheduleResponse> reservationSchedules
) {

    public record ReservationScheduleResponse(
        long reservationScheduleId,
        String roundName,
        String reserveAt
    ) {

        public static ReservationScheduleResponse from(ConcertDetailDTO.ReservationScheduleDTO dto) {
            return new ReservationScheduleResponse(
                dto.reservationScheduleId(),
                dto.roundName(),
                DateConvertor.convertToDefaultFormat(dto.reserveAt())
            );
        }
    }

    public static ConcertDetailInfoResponse of(ConcertDetailDTO concertDetailDTO,
        boolean isFavorite) {
        return new ConcertDetailInfoResponse(
            concertDetailDTO.concertId(),
            concertDetailDTO.posterUrl(),
            concertDetailDTO.title(),
            DateConvertor.convertToDefaultFormat(concertDetailDTO.startAt()),
            DateConvertor.convertToDefaultFormat(concertDetailDTO.endAt()),
            concertDetailDTO.area(),
            concertDetailDTO.time(),
            concertDetailDTO.ageRating(),
            concertDetailDTO.price(),
            concertDetailDTO.address(),
            isFavorite,
            concertDetailDTO.reservations().stream()
                .map(ConcertReservationResponse::from)
                .toList(),
            concertDetailDTO.reservationSchedules().stream()
                .map(ReservationScheduleResponse::from)
                .toList()
        );
    }
}
