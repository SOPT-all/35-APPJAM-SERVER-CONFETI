package org.sopt.confeti.api.user.dto.response.timetable;

import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableExistenceDTO;

public record TimetableExistenceResponse(
    boolean hasTimetable
) {

    public static TimetableExistenceResponse from(
        TimetableExistenceDTO timetableExistenceDTO
    ) {
        return new TimetableExistenceResponse(
            timetableExistenceDTO.hasTimetable()
        );
    }
}
