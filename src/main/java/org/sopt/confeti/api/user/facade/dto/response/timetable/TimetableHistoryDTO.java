package org.sopt.confeti.api.user.facade.dto.response.timetable;

public record TimetableHistoryDTO(
    boolean hasTimetableHistory
) {

    public static TimetableHistoryDTO from(boolean hasTimetableHistory) {
        return new TimetableHistoryDTO(
            hasTimetableHistory
        );
    }
}
