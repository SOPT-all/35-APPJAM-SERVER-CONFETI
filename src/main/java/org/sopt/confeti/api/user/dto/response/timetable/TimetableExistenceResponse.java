package org.sopt.confeti.api.user.dto.response.timetable;

import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableExistenceDTO;

public record TimetableExistenceResponse(
    Long timetableId
) {

    public static TimetableExistenceResponse from(
        TimetableExistenceDTO timetableExistenceDTO
    ) {
        return new TimetableExistenceResponse(
            timetableExistenceDTO.timetableId()
        );
    }
}
