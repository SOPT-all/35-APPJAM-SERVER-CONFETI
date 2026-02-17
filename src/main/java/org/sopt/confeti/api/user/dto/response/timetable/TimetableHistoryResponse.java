package org.sopt.confeti.api.user.dto.response.timetable;

import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableHistoryDTO;

public record TimetableHistoryResponse(
    boolean hasTimetableHistory
) {

    public static TimetableHistoryResponse from(TimetableHistoryDTO timetableHistoryDTO) {
        return new TimetableHistoryResponse(
            timetableHistoryDTO.hasTimetableHistory()
        );
    }
}
