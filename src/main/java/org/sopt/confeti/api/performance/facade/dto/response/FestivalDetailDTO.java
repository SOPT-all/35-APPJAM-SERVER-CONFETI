package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.performance.Performance;

public record FestivalDetailDTO(
        long festivalId,
        String title,
        String subtitle,
        LocalDate startAt,
        LocalDate endAt,
        String area,
        String posterPath,
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
    public static FestivalDetailDTO of(final Performance performance, boolean isFavorite) {
        return new FestivalDetailDTO(
                performance.getId(),
                performance.getTitle(),
                performance.getSubtitle(),
                performance.getStartAt(),
                performance.getEndAt(),
                performance.getArea(),
                performance.getPosterPath(),
                performance.getLogoPath(),
                performance.getReserveAt(),
                performance.getAgeRating(),
                performance.getTime(),
                performance.getPrice(),
                isFavorite,
                performance.getAddress(),
                performance.getReservationUrls().stream()
                        .map(FestivalReservationDTO::from)
                        .toList(),
                performance.getSchedules().stream()
                        .map(FestivalDetailDateDTO::from)
                        .toList()
        );
    }
}
