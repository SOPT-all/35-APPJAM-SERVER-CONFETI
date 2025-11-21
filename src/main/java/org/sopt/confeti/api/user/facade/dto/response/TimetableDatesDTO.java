package org.sopt.confeti.api.user.facade.dto.response;


import java.util.List;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.timetable_festival.TimetableFestival;

public record TimetableDatesDTO(
    Long timetableFestivalId,
    String title,
    String posterUrl,
    List<TimetableDateDTO> dates
) {

    public static TimetableDatesDTO of(
        TimetableFestival timetableFestival,
        Festival festival,
        List<FestivalDate> festivalDates
    ) {
        List<TimetableDateDTO> dates = festivalDates.stream()
            .map(TimetableDateDTO::from)
            .toList();

        return new TimetableDatesDTO(
            timetableFestival.getId(), festival.getTitle(), festival.getPosterPath(), dates);
    }

}
