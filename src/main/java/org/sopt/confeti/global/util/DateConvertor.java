package org.sopt.confeti.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.sopt.confeti.annotation.Convertor;

@Convertor
public class DateConvertor {

    private final DateTimeFormatter spotifyLocalDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter localDateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private final DateTimeFormatter localTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter localDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public String convert(final LocalDate localDate) {
        return localDate.format(localDateFormat);
    }

    public String convert(final LocalTime localTime) {
        return localTime.format(localTimeFormat);
    }

    public String convert(final LocalDateTime localDateTime) {
        return localDateTime.format(localDateTimeFormat);
    }

    public LocalDate convertToLocalDate(final String localDate) {
        return LocalDate.parse(localDate, localDateFormat);
    }

    public LocalTime convertToLocalTime(final String localTime) {
        return LocalTime.parse(localTime, localTimeFormat);
    }

    public LocalDateTime convertToLocalDateTime(final String localDateTime) {
        return LocalDateTime.parse(localDateTime, localDateTimeFormat);
    }

    public LocalDate convertToSpotifyLocalDate(final String spotifyLocalDate) {
        return LocalDate.parse(spotifyLocalDate, spotifyLocalDateFormat);
    }
}
