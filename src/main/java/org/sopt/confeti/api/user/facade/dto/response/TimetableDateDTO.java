package org.sopt.confeti.api.user.facade.dto.response;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import org.sopt.confeti.domain.festival_date.FestivalDate;

public record TimetableDateDTO(
    long festivalDateId,
    LocalDate festivalAt,
    String dayOfWeek,
    String displayedDayOfWeek
) {

    public static TimetableDateDTO from(FestivalDate festivalDate) {
        LocalDate festivalAt = festivalDate.getFestivalAt();
        DayOfWeek dayOfWeek = festivalAt.getDayOfWeek();
        String displayedDayOfWeek = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN);

        return new TimetableDateDTO(
            festivalDate.getId(), festivalAt, dayOfWeek.toString(), displayedDayOfWeek);
    }
}
