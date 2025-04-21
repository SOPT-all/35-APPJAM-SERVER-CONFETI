package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.timetable_festival.TimetableFestival;

public record UserTimetablesDTO(
        List<UserTimetableDTO> timetables
) {
    public static UserTimetablesDTO from(List<TimetableFestival> timetableFestivals) {
        return new UserTimetablesDTO(
                timetableFestivals.stream()
                        .map(UserTimetableDTO::from)
                        .toList()
        );
    }
}