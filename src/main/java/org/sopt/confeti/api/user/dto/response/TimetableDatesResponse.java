package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.TimetableDatesDTO;

public record TimetableDatesResponse(
    Long timetableFestivalId,
    String title,
    String posterUrl,
    List<TimetableDateResponse> dates
) {

    public static TimetableDatesResponse from(TimetableDatesDTO timetableDatesDTO) {
        List<TimetableDateResponse> dates = timetableDatesDTO.dates().stream()
            .map(TimetableDateResponse::from)
            .toList();
        return new TimetableDatesResponse(
            timetableDatesDTO.timetableFestivalId(),
            timetableDatesDTO.title(),
            timetableDatesDTO.posterUrl(),
            dates
        );
    }
}
