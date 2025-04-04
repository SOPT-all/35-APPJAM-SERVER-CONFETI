package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.domain.festival.Festival;

public record FestivalDetailDTO(
        long festivalId,
        String title,
        String subtitle,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String area,
        String posterPath,
        String posterBgPath,
        String logoPath,
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
                festival.getId(),
                festival.getTitle(),
                festival.getSubtitle(),
                festival.getStartAt(),
                festival.getEndAt(),
                festival.getArea(),
                festival.getPosterPath(),
                festival.getPosterBgPath(),
                festival.getLogoPath(),
                festival.getReserveAt(),
                festival.getAgeRating(),
                festival.getTime(),
                festival.getPrice(),
                isFavorite,
                festival.getAddress(),
                festival.getReservationUrls().stream()
                        .map(FestivalReservationDTO::from)
                        .toList(),
                festival.getDates().stream()
                        .map(FestivalDetailDateDTO::from)
                        .toList()
        );
    }
}
