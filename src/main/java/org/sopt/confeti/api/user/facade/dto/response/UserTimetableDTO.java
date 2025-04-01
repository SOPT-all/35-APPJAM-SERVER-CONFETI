package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.timetable_festival.TimetableFestival;

public record UserTimetableDTO(
        List<UserTimetableFestivalDTO> festivals
) {
    public static UserTimetableDTO from(List<TimetableFestival> userTimetable) {
        return new UserTimetableDTO(
                userTimetable.stream()
                        .map(UserTimetableFestivalDTO::from)
                        .toList()
        );
    }
}
