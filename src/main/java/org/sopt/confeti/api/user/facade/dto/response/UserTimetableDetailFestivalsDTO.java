package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.timetable_festival.TimetableFestival;

public record UserTimetableDetailFestivalsDTO(
        List<UserTimetableDetailFestivalDTO> festivals
) {
    public static UserTimetableDetailFestivalsDTO from(List<TimetableFestival> userTimetable) {
        return new UserTimetableDetailFestivalsDTO(
                userTimetable.stream()
                        .map(UserTimetableDetailFestivalDTO::from)
                        .toList()
        );
    }
}
