package org.sopt.confeti.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateConvertor {

    private static final DateTimeFormatter spotifyLocalDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter localDateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private static final DateTimeFormatter localTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter localDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static String convert(final LocalDate localDate) {
        if (localDate == null) {
            return null;
        }

        return localDate.format(localDateFormat);
    }

    public static String convert(final LocalTime localTime) {
        if (localTime == null) {
            return null;
        }

        return localTime.format(localTimeFormat);
    }

    public static String convert(final LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        return localDateTime.format(localDateTimeFormat);
    }

    public static LocalDate convertToLocalDate(final String localDate) {
        return LocalDate.parse(localDate, localDateFormat);
    }

    public static LocalTime convertToLocalTime(final String localTime) {
        return LocalTime.parse(localTime, localTimeFormat);
    }

    public static LocalDateTime convertToLocalDateTime(final String localDateTime) {
        return LocalDateTime.parse(localDateTime, localDateTimeFormat);
    }

    public static LocalDate convertToSpotifyLocalDate(final String spotifyLocalDate) {
        return LocalDate.parse(spotifyLocalDate, spotifyLocalDateFormat);
    }
}
