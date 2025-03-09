package org.sopt.confeti.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateConvertor {

    private static final DateTimeFormatter SPOTIFY_LOCAL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DEFAULT_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static String convertToDefaultFormat(final LocalDate localDate) {
        if (localDate == null) {
            return null;
        }

        return LocalDateTime.of(localDate, LocalTime.MIN).format(DEFAULT_TIME_FORMAT);
    }

    public static String convertToDefaultFormat(final LocalDate localDate, final LocalTime localTime) {
        if (localDate == null || localTime == null) {
            return null;
        }

        return LocalDateTime.of(localDate, localTime).format(DEFAULT_TIME_FORMAT);
    }

    public static String convertToDefaultFormat(final LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        return localDateTime.format(DEFAULT_TIME_FORMAT);
    }

    public static LocalDate convertToSpotifyLocalDate(final String spotifyLocalDate) {
        return LocalDate.parse(spotifyLocalDate, SPOTIFY_LOCAL_DATE_FORMAT);
    }
}
