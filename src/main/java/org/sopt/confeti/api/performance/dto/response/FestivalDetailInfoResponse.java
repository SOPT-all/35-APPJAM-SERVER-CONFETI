package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.global.util.DateConvertor;

public record FestivalDetailInfoResponse(
        long festivalId,
        String posterUrl,
        String posterBgUrl,
        String title,
        String subtitle,
        String startAt,
        String endAt,
        String area,
        String reserveAt,
        String time,
        String ageRating,
        String price,
        boolean isFavorite,
        String address,
        List<FestivalReservationResponse> reservations
) {
    public static FestivalDetailInfoResponse from(final FestivalDetailDTO festival) {
        return new FestivalDetailInfoResponse(
                festival.festivalId(),
                festival.posterUrl(),
                festival.posterBgUrl(),
                festival.title(),
                festival.subtitle(),
                DateConvertor.convertToDefaultFormat(festival.startAt()),
                DateConvertor.convertToDefaultFormat(festival.endAt()),
                festival.area(),
                DateConvertor.convertToDefaultFormat(festival.reserveAt()),
                festival.time(),
                festival.ageRating(),
                festival.price(),
                festival.isFavorite(),
                festival.address(),
                festival.reservations().stream()
                        .map(FestivalReservationResponse::from)
                        .toList()
        );
    }
}
