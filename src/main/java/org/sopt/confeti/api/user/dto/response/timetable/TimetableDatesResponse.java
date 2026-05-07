package org.sopt.confeti.api.user.dto.response.timetable;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableDatesDTO;

public record TimetableDatesResponse(
    long timetableFestivalId,
    String title,
    String posterUrl,
    List<TimetableDateResponse> dates
) {

    public static TimetableDatesResponse from(TimetableDatesDTO timetableDatesDTO) {
        List<TimetableDateResponse> dates = timetableDatesDTO.dates().stream()
            .map(TimetableDateResponse::from)
            .toList();

        return new TimetableDatesResponse(
            timetableDatesDTO.timetableId(),
            timetableDatesDTO.title(),
            timetableDatesDTO.posterUrl(),
            dates
        );
    }
}
