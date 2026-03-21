package org.sopt.confeti.api.user.facade.dto.response.timetable;

public record TimetableExistenceDTO(
    boolean hasTimetable,
    Long timetableId
) {

    public static TimetableExistenceDTO of(boolean hasTimetable, Long timetableId) {
        return new TimetableExistenceDTO(
            hasTimetable,
            timetableId
        );
    }
}
