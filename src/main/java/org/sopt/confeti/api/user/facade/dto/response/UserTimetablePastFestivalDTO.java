package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.timetable_festival.TimetableFestival;
import java.time.LocalDate;
import java.util.List;

public record UserTimetablePastFestivalDTO (
        String logoPath,
        String title,
        LocalDate startAt,
        LocalDate endAt,
        String area,
        List<UserTimetableDatesDTO> festivalDates
){
    public static UserTimetablePastFestivalDTO from(TimetableFestival timetableFestival) {
        return new UserTimetablePastFestivalDTO(
                timetableFestival.getFestival().getLogoPath(),
                timetableFestival.getFestival().getTitle(),
                timetableFestival.getFestival().getStartAt(),
                timetableFestival.getFestival().getEndAt(),
                timetableFestival.getFestival().getArea(),
                timetableFestival.getFestival().getDates().stream()
                        .map(UserTimetableDatesDTO::from)
                        .toList()
        );
    }
}