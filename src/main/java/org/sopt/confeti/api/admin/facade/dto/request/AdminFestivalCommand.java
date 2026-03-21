package org.sopt.confeti.api.admin.facade.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;

public record AdminFestivalCommand(
    Long festivalId,
    String title,
    String subtitle,
    LocalDate startAt,
    LocalDate endAt,
    String area,
    LocalDateTime reserveAt,
    String ageRating,
    String time,
    String price,
    String address,
    TimetableSupportStatus timetableSupportStatus,
    List<ReservationUrlCommand> reservationUrls,
    List<DateCommand> dates
) {

    public static AdminFestivalCommand of(
        Long festivalId, String title, String subtitle,
        LocalDate startAt, LocalDate endAt, String area,
        LocalDateTime reserveAt, String ageRating, String time,
        String price, String address, TimetableSupportStatus timetableSupportStatus,
        List<ReservationUrlCommand> reservationUrls,
        List<DateCommand> dates
    ) {
        return new AdminFestivalCommand(
            festivalId, title, subtitle, startAt, endAt, area,
            reserveAt, ageRating, time, price, address, timetableSupportStatus,
            reservationUrls, dates
        );
    }

    public record ReservationUrlCommand(
        Long ticketVendorId,
        String reservationUrl
    ) {

        public static ReservationUrlCommand of(Long ticketVendorId, String reservationUrl) {
            return new ReservationUrlCommand(ticketVendorId, reservationUrl);
        }
    }

    public record DateCommand(
        Long festivalDateId,
        LocalDate festivalAt,
        LocalTime openAt,
        List<String> artistIds,
        List<StageCommand> stages
    ) {

        public static DateCommand of(Long festivalDateId, LocalDate festivalAt,
            LocalTime openAt, List<String> artistIds, List<StageCommand> stages) {
            return new DateCommand(festivalDateId, festivalAt, openAt, artistIds, stages);
        }
    }

    public record StageCommand(
        Long festivalStageId,
        String name,
        int order,
        List<TimeCommand> times
    ) {

        public static StageCommand of(Long festivalStageId, String name, int order,
            List<TimeCommand> times) {
            return new StageCommand(festivalStageId, name, order, times);
        }
    }

    public record TimeCommand(
        Long festivalTimeId,
        LocalTime startAt,
        LocalTime endAt,
        String name,
        List<String> artistIds
    ) {

        public static TimeCommand of(Long festivalTimeId, LocalTime startAt, LocalTime endAt,
            String name, List<String> artistIds) {
            return new TimeCommand(festivalTimeId, startAt, endAt, name, artistIds);
        }
    }
}
