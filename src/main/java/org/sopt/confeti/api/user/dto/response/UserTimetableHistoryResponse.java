package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserTimetableHistoryDTO;

public record UserTimetableHistoryResponse(
        boolean hasTimetableHistory
) {
    public static UserTimetableHistoryResponse from(UserTimetableHistoryDTO timetableHistoryDTO) {
        return new UserTimetableHistoryResponse(
                timetableHistoryDTO.hasTimetableHistory()
        );
    }
}
