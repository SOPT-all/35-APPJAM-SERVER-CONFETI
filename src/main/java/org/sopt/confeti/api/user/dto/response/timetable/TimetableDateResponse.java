package org.sopt.confeti.api.user.dto.response.timetable;

import java.time.LocalDate;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableDateDTO;

public record TimetableDateResponse(
    long festivalDateId,
    LocalDate festivalAt,
    String dayOfWeek,
    String displayedDayOfWeek
) {

    public static TimetableDateResponse from(TimetableDateDTO timetableDateDTO) {
        return new TimetableDateResponse(
            timetableDateDTO.festivalDateId(),
            timetableDateDTO.festivalAt(),
            timetableDateDTO.dayOfWeek(),
            timetableDateDTO.displayedDayOfWeek()
        );
    }

}
