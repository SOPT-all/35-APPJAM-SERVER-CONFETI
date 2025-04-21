package org.sopt.confeti.api.user.facade.dto.response;

public record UserTimetableHistoryDTO(
        boolean hasTimetableHistory
) {
    public static UserTimetableHistoryDTO from(boolean hasTimetableHistory) {
        return new UserTimetableHistoryDTO(
                hasTimetableHistory
        );
    }
}
