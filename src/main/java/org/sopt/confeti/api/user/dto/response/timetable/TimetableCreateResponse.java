package org.sopt.confeti.api.user.dto.response.timetable;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableCreateResponseDTO;

public record TimetableCreateResponse(
    List<Long> timetableIds
) {

    public static TimetableCreateResponse from(TimetableCreateResponseDTO dto) {
        return new TimetableCreateResponse(dto.timetableIds());
    }
}
