package org.sopt.confeti.api.user.dto.response.timetable;

import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableToAddDTO;

public record TimetablesToAddFestivalResponse(
    long festivalId,
    String posterUrl,
    String title
) {

    public static TimetablesToAddFestivalResponse from(final TimetableToAddDTO timetableToAddDTO) {
        return new TimetablesToAddFestivalResponse(
            timetableToAddDTO.festivalId(),
            timetableToAddDTO.posterUrl(),
            timetableToAddDTO.title()
        );
    }
}
