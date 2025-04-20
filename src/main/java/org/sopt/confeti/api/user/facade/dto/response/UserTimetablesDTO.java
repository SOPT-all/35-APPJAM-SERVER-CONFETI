package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.timetable_festival.TimetableFestival;

import java.util.List;

public record UserTimetablesDTO(
        List<UserTimetableDTO> timetables
){
    public static UserTimetablesDTO from(List<TimetableFestival> timetableFestivals) {
        return new UserTimetablesDTO(
                timetableFestivals.stream()
                        .map(UserTimetableDTO::from)
                        .toList()
        );
    }
}