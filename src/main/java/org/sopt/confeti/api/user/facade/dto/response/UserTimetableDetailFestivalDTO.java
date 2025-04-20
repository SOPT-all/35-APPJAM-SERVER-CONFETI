package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.timetable_festival.TimetableFestival;

public record UserTimetableDetailFestivalDTO(
        long festivalId,
        String title,
        String logoPath,
        List<UserTimetableDatesDTO> festivalDates
) {
    public static UserTimetableDetailFestivalDTO from(TimetableFestival timetableFestival) {
        return new UserTimetableDetailFestivalDTO(
                timetableFestival.getFestival().getId(),
                timetableFestival.getFestival().getTitle(),
                timetableFestival.getFestival().getLogoPath(),
                timetableFestival.getFestival().getDates().stream()
                        .map(UserTimetableDatesDTO::from)
                        .toList()
        );
    }

}
