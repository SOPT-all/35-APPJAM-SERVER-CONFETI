package org.sopt.confeti.api.user.facade.dto.response.timetable;

import org.sopt.confeti.domain.festival.Festival;

public record TimetableToAddDTO(
    long festivalId,
    String posterPath,
    String title
) {

    public static TimetableToAddDTO from(final Festival festival) {
        return new TimetableToAddDTO(
            festival.getId(),
            festival.getPosterPath(),
            festival.getTitle()
        );
    }
}
