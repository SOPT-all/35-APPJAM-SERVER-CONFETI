package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.global.util.DateConvertor;

public record ConcertDetailInfoResponse(
        long concertId,
        String posterUrl,
        String title,
        String subtitle,
        String startAt,
        String endAt,
        String area,
        String reserveAt,
        String time,
        String ageRating,
        String price,
        String address,
        boolean isFavorite,
        List<ConcertReservationResponse> reservations
) {
    public static ConcertDetailInfoResponse from(ConcertDetailDTO concertDetailDTO) {
        return new ConcertDetailInfoResponse(
                concertDetailDTO.concertId(),
                concertDetailDTO.posterUrl(),
                concertDetailDTO.title(),
                concertDetailDTO.subtitle(),
                DateConvertor.convertToDefaultFormat(concertDetailDTO.startAt()),
                DateConvertor.convertToDefaultFormat(concertDetailDTO.endAt()),
                concertDetailDTO.area(),
                DateConvertor.convertToDefaultFormat(concertDetailDTO.reserveAt()),
                concertDetailDTO.time(),
                concertDetailDTO.ageRating(),
                concertDetailDTO.price(),
                concertDetailDTO.address(),
                concertDetailDTO.isFavorite(),
                concertDetailDTO.reservations().stream()
                        .map(ConcertReservationResponse::from)
                        .toList()
        );
    }
}
