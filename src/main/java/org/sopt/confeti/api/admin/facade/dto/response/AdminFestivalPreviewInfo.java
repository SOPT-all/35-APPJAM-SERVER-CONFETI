package org.sopt.confeti.api.admin.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.FestivalFileInfo;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;

@Builder(toBuilder = true)
public record AdminFestivalPreviewInfo(
    long festivalId,
    String title,
    LocalDate startAt,
    LocalDate endAt,
    String area,
    String posterPath,
    String posterUrl,
    String logoPath,
    String logoUrl,
    String ageRating,
    String time,
    String price,
    String address,
    TimetableSupportStatus timetableSupportStatus,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static AdminFestivalPreviewInfo from(Festival festival) {
        return AdminFestivalPreviewInfo.builder()
            .festivalId(festival.getId())
            .title(festival.getTitle())
            .startAt(festival.getStartAt())
            .endAt(festival.getEndAt())
            .area(festival.getArea())
            .posterPath(festival.getPosterPath())
            .logoPath(festival.getLogoPath())
            .ageRating(festival.getAgeRating())
            .time(festival.getTime())
            .price(festival.getPrice())
            .address(festival.getAddress())
            .timetableSupportStatus(festival.getTimetableSupportStatus())
            .createdAt(festival.getCreatedAt())
            .updatedAt(festival.getUpdatedAt())
            .build();
    }

    public AdminFestivalPreviewInfo withFileUrls(FestivalFileInfo festivalFileInfo) {
        return this.toBuilder()
            .posterUrl(festivalFileInfo.posterUrl())
            .logoUrl(festivalFileInfo.logoUrl())
            .build();
    }
}
