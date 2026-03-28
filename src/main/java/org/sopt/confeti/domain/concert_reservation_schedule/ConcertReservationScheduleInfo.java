package org.sopt.confeti.domain.concert_reservation_schedule;

import java.time.LocalDateTime;

public record ConcertReservationScheduleInfo(
    Long id,
    String roundName,
    LocalDateTime reserveAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ConcertReservationScheduleInfo of(
        String roundName, LocalDateTime reserveAt
    ) {
        return new ConcertReservationScheduleInfo(null, roundName, reserveAt, null, null);
    }
}
