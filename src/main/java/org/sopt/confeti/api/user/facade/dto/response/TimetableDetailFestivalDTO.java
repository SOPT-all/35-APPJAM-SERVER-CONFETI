package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.timetable.Timetable;

public record TimetableDetailFestivalDTO(
        long festivalId,
        String title,
        String logoPath,
        List<TimetableFestivalDateDTO> festivalDates
) {
    public static TimetableDetailFestivalDTO from(Timetable timetable) {
        return new TimetableDetailFestivalDTO(
                timetable.getFestival().getId(),
                timetable.getFestival().getTitle(),
                timetable.getFestival().getLogoPath(),
                timetable.getFestival().getDates().stream()
                        .map(TimetableFestivalDateDTO::from)
                        .toList()
        );
    }

}
