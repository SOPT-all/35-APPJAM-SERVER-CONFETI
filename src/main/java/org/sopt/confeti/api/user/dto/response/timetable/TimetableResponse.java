package org.sopt.confeti.api.user.dto.response.timetable;

import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableDTO;

public record TimetableResponse(
    long typeId,
    String posterUrl,
    String title
) {

    public static TimetableResponse from(final TimetableDTO timetableDTO) {
        return new TimetableResponse(
            timetableDTO.typeId(),
            timetableDTO.posterUrl(),
            timetableDTO.title()
        );
    }
}
