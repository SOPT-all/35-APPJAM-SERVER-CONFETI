package org.sopt.confeti.domain.concert.application.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.sopt.confeti.domain.concert.Concert;

public record ConcertPreviewInfo(
    long concertId,
    String title,
    LocalDate startAt,
    LocalDate endAt,
    String area,
    String posterPath,
    LocalDateTime reserveAt,
    String ageRating,
    String time,
    String price,
    String address,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ConcertPreviewInfo from(Concert concert) {
        return new ConcertPreviewInfo(
            concert.getId(),
            concert.getTitle(),
            concert.getStartAt(),
            concert.getEndAt(),
            concert.getArea(),
            concert.getPosterPath(),
            concert.getReserveAt(),
            concert.getAgeRating(),
            concert.getTime(),
            concert.getPrice(),
            concert.getAddress(),
            concert.getCreatedAt(),
            concert.getUpdatedAt()
        );
    }
}
