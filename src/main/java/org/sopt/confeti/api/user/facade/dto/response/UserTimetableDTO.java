package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.timetablefestival.TimetableFestival;

import java.util.List;

public record UserTimetableDTO (
        List<UserTimetableFestivalDTO> festivals
){
    public static UserTimetableDTO from(List<TimetableFestival> userTimetable) {
        return new UserTimetableDTO(
                userTimetable.stream()
                        .map(UserTimetableFestivalDTO::from)
                        .toList()
        );
    }
}
