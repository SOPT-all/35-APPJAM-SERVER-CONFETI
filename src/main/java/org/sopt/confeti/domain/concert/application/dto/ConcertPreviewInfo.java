package org.sopt.confeti.domain.concert.application.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import org.sopt.confeti.domain.concert.Concert;

@Builder(toBuilder = true)
public record ConcertPreviewInfo(
    long concertId,
    String title,
    LocalDate startAt,
    LocalDate endAt,
    String area,
    String posterPath,
    String posterUrl,
    String ageRating,
    String time,
    String price,
    String address,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ConcertPreviewInfo from(Concert concert) {
        return ConcertPreviewInfo.builder()
            .concertId(concert.getId())
            .title(concert.getTitle())
            .startAt(concert.getStartAt())
            .endAt(concert.getEndAt())
            .area(concert.getArea())
            .posterPath(concert.getPosterPath())
            .ageRating(concert.getAgeRating())
            .time(concert.getTime())
            .price(concert.getPrice())
            .address(concert.getAddress())
            .createdAt(concert.getCreatedAt())
            .updatedAt(concert.getUpdatedAt())
            .build();
    }

    public ConcertPreviewInfo withFileUrls(ConcertFileInfo fileUrls) {
        return this.toBuilder()
            .posterUrl(fileUrls.posterUrl())
            .build();
    }
}
