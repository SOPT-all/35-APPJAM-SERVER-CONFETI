package org.sopt.confeti.api.user.facade.dto.response.timetable;

public record TimetableExistenceDTO(
    Long timetableId
) {

    public static TimetableExistenceDTO from(Long timetableId) {
        return new TimetableExistenceDTO(
            timetableId
        );
    }

    public static TimetableExistenceDTO notExists() {
        return new TimetableExistenceDTO(
            null
        );
    }
}
