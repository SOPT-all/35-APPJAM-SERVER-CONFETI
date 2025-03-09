package org.sopt.confeti.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateConvertor {

    private static final DateTimeFormatter SPOTIFY_LOCAL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DEFAULT_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static String convert(final LocalDate localDate) {
        if (localDate == null) {
            return null;
        }

        return localDate.format(DEFAULT_TIME_FORMAT);
    }

    public static String convert(final LocalTime localTime) {
        if (localTime == null) {
            return null;
        }

        return localTime.format(DEFAULT_TIME_FORMAT);
    }

    public static String convert(final LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        return localDateTime.format(DEFAULT_TIME_FORMAT);
    }

    public static String convertToLocalDate(final LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        return localDateTime.format(DEFAULT_TIME_FORMAT);
    }

    public static LocalDate convertToSpotifyLocalDate(final String spotifyLocalDate) {
        return LocalDate.parse(spotifyLocalDate, SPOTIFY_LOCAL_DATE_FORMAT);
    }
}
