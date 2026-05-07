package org.sopt.confeti.api.user.facade.dto.response.timetable;

import java.util.List;

public record TimetablesDTO(
    List<TimetableDTO> timetables
) {

    public static TimetablesDTO from(List<TimetableDTO> timetables) {
        return new TimetablesDTO(timetables);
    }
}
