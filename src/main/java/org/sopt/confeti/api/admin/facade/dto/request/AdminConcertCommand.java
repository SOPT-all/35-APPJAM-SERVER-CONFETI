package org.sopt.confeti.api.admin.facade.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record AdminConcertCommand(
    Long concertId,
    String title,
    LocalDate startAt,
    LocalDate endAt,
    String area,
    String ageRating,
    String time,
    String price,
    String address,
    List<String> artistIds,
    List<ReservationUrl> reservationUrls,
    List<ReservationScheduleCommand> reservationSchedules
) {

    public static AdminConcertCommand of(
        Long concertId, String title,
        LocalDate startAt, LocalDate endAt, String area,
        String ageRating, String time,
        String price, String address, List<String> artistIds,
        List<ReservationUrl> reservationUrls,
        List<ReservationScheduleCommand> reservationSchedules
    ) {
        return new AdminConcertCommand(
            concertId, title, startAt, endAt, area,
            ageRating, time, price, address, artistIds,
            reservationUrls, reservationSchedules
        );
    }

    public record ReservationUrl(Long ticketVendorId, String reservationUrl) {

        public static ReservationUrl of(Long ticketVendorId, String reservationUrl) {
            return new ReservationUrl(ticketVendorId, reservationUrl);
        }
    }

    public record ReservationScheduleCommand(String roundName, LocalDateTime reserveAt) {

        public static ReservationScheduleCommand of(String roundName, LocalDateTime reserveAt) {
            return new ReservationScheduleCommand(roundName, reserveAt);
        }
    }
}
