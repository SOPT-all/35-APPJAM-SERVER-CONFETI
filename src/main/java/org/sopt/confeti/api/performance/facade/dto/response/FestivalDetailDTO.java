package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.global.annotation.RedisSerializable;

@RedisSerializable
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
    String address,
    List<FestivalReservationDTO> reservations,
    List<FestivalDetailDateDTO> dates
) {

    public static FestivalDetailDTO from(Festival festival) {
        return new FestivalDetailDTO(
            festival.getId(),
            festival.getTitle(),
            festival.getSubtitle(),
            festival.getStartAt(),
            festival.getEndAt(),
            festival.getArea(),
            festival.getPosterPath(),
            festival.getLogoPath(),
            festival.getReserveAt(),
            festival.getAgeRating(),
            festival.getTime(),
            festival.getPrice(),
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
