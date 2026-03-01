package org.sopt.confeti.api.admin.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;

public record AdminFestivalPreviewInfo(
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
    TimetableSupportStatus timetableSupportStatus,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static AdminFestivalPreviewInfo from(Festival festival) {
        return new AdminFestivalPreviewInfo(
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
            festival.getTimetableSupportStatus(),
            festival.getCreatedAt(),
            festival.getUpdatedAt()
        );
    }
}
