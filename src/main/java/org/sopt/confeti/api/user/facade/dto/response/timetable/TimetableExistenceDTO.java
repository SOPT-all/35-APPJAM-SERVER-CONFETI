package org.sopt.confeti.api.user.facade.dto.response.timetable;

public record TimetableExistenceDTO(
    boolean hasTimetable
) {

    public static TimetableExistenceDTO from(boolean hasTimetable) {
        return new TimetableExistenceDTO(
            hasTimetable
        );
    }
}
