package org.sopt.confeti.api.user.facade.dto.response.timetable;

import java.util.List;
import org.sopt.confeti.domain.timetable.Timetable;

public record TimetableCreateResponseDTO(
    List<Long> timetableIds
) {

    public static TimetableCreateResponseDTO of(List<Timetable> timetables) {
        return new TimetableCreateResponseDTO(
            timetables.stream()
                .map(Timetable::getId)
                .toList()
        );
    }
}
