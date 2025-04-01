package org.sopt.confeti.api.user.facade.dto.response;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.sopt.confeti.domain.festival_time.FestivalTime;
import org.sopt.confeti.domain.user_timetable.UserTimetable;


public record UserTimetableFestivalTimeDTO(
        long userTimetableId,
        LocalTime startAt,
        LocalTime endAt,
        boolean isSelected,
        List<UserTimetableFestivalArtistDTO> artists
) {
    public static UserTimetableFestivalTimeDTO of(FestivalTime festivalTime, Map<Long, UserTimetable> userTimetables) {
        return new UserTimetableFestivalTimeDTO(
                userTimetables.get(festivalTime.getId()).getId(),
                festivalTime.getStartAt(),
                festivalTime.getEndAt(),
                userTimetables.get(festivalTime.getId()).isSelected(),
                festivalTime.getArtists()
                        .stream()
                        .map(UserTimetableFestivalArtistDTO::from)
                        .toList()
        );
    }
}