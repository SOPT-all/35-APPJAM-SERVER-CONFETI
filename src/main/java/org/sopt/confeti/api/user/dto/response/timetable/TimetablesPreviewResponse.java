package org.sopt.confeti.api.user.dto.response.timetable;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetablesDTO;

public record TimetablesPreviewResponse(
    List<TimetableResponse> timetables
) {

    public static TimetablesPreviewResponse from(final TimetablesDTO timetableDTO) {
        return new TimetablesPreviewResponse(
            timetableDTO.timetables().stream()
                .map(TimetableResponse::from)
                .toList()
        );
    }
}
