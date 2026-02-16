package org.sopt.confeti.api.user.facade.dto.response.timetable;

import java.util.List;
import org.sopt.confeti.domain.timetable.Timetable;

public record TimetablesDTO(
    List<TimetableDTO> timetables
) {

    public static TimetablesDTO from(List<Timetable> timetables) {
        return new TimetablesDTO(
            timetables.stream()
                .map(TimetableDTO::from)
                .toList()
        );
    }
}
