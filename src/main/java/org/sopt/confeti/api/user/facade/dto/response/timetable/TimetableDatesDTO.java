package org.sopt.confeti.api.user.facade.dto.response.timetable;


import java.util.List;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.timetable.Timetable;

public record TimetableDatesDTO(
    long timetableId,
    String title,
    String posterUrl,
    List<TimetableDateDTO> dates
) {

    public static TimetableDatesDTO of(
        Timetable timetable,
        Festival festival,
        List<FestivalDate> festivalDates
    ) {
        List<TimetableDateDTO> dates = festivalDates.stream()
            .map(TimetableDateDTO::from)
            .toList();

        return new TimetableDatesDTO(
            timetable.getId(), festival.getTitle(), festival.getPosterPath(), dates);
    }

}
