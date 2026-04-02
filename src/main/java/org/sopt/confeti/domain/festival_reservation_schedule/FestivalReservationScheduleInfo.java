package org.sopt.confeti.domain.festival_reservation_schedule;

import java.time.LocalDateTime;

public record FestivalReservationScheduleInfo(
    Long id,
    String roundName,
    LocalDateTime reserveAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static FestivalReservationScheduleInfo of(
        String roundName, LocalDateTime reserveAt
    ) {
        return new FestivalReservationScheduleInfo(null, roundName, reserveAt, null, null);
    }
}
