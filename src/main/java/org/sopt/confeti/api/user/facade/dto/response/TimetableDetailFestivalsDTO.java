package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.timetable.Timetable;

public record TimetableDetailFestivalsDTO(
        List<TimetableDetailFestivalDTO> festivals
) {
    public static TimetableDetailFestivalsDTO from(List<Timetable> timetables) {
        return new TimetableDetailFestivalsDTO(
                timetables.stream()
                        .map(TimetableDetailFestivalDTO::from)
                        .toList()
        );
    }
}
