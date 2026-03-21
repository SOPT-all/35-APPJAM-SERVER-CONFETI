package org.sopt.confeti.api.user.facade.dto.response.timetable;

public record TimetableExistenceDTO(
    boolean hasTimetable,
    Long timetableId
) {

    public static TimetableExistenceDTO from(Long timetableId) {
        return new TimetableExistenceDTO(
            true,
            timetableId
        );
    }

    public static TimetableExistenceDTO notExists() {
        return new TimetableExistenceDTO(
            false,
            null
        );
    }
}
