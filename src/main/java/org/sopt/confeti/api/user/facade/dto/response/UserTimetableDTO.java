package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.timetable_festival.TimetableFestival;

public record UserTimetableDTO(
        Long typeId,
        String posterPath,
        String title
) {
    public static UserTimetableDTO from(final TimetableFestival timetableFestival) {
        return new UserTimetableDTO(
                timetableFestival.getFestival().getId(),
                timetableFestival.getFestival().getPosterPath(),
                timetableFestival.getFestival().getTitle()
        );
    }
}