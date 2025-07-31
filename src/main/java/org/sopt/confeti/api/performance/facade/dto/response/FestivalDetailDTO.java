package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.global.mapper.dto.festival.Festival;

public record FestivalDetailDTO(
        long festivalId,
        String title,
        String subtitle,
        LocalDate startAt,
        LocalDate endAt,
        String area,
        String posterUrl,
        String logoUrl,
        LocalDateTime reserveAt,
        String ageRating,
        String time,
        String price,
        boolean isFavorite,
        String address,
        List<FestivalReservationDTO> reservations,
        List<FestivalDetailDateDTO> dates
) {
    public static FestivalDetailDTO of(final Festival festival, boolean isFavorite) {
        return new FestivalDetailDTO(
                festival.performanceId(),
                festival.title(),
                festival.subtitle(),
                festival.startAt(),
                festival.endAt(),
                festival.area(),
                festival.posterUrl(),
                festival.logoUrl(),
                festival.reserveAt(),
                festival.ageRating(),
                festival.time(),
                festival.price(),
                isFavorite,
                festival.address(),
                festival.reservations().stream()
                        .map(FestivalReservationDTO::from)
                        .toList(),
                festival.dates().stream()
                        .map(FestivalDetailDateDTO::from)
                        .toList()
        );
    }
}
