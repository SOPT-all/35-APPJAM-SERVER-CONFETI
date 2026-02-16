package org.sopt.confeti.api.user.facade.dto.response.timetable;

import org.sopt.confeti.domain.timetable.Timetable;

public record TimetableDTO(
    Long typeId,
    String posterPath,
    String title
) {

    public static TimetableDTO from(final Timetable timetable) {
        return new TimetableDTO(
            timetable.getFestival().getId(),
            timetable.getFestival().getPosterPath(),
            timetable.getFestival().getTitle()
        );
    }
}
