package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.festivaltime.FestivalTime;
import org.sopt.confeti.domain.usertimetable.UserTimetable;

import java.time.LocalTime;
import java.util.List;


public record UserTimetableFestivalTimeDTO (
        long festivalTimeId,
        LocalTime startAt,
        LocalTime endAt,
        boolean isSelected,
        List<UserTimetableFestivalArtistDTO> artists
){
    public static UserTimetableFestivalTimeDTO from(FestivalTime festivalTime) {
        return new UserTimetableFestivalTimeDTO(
                festivalTime.getId(),
                festivalTime.getStartAt(),
                festivalTime.getEndAt(),
                festivalTime.getTimetables()
                        .stream()
                        .anyMatch(UserTimetable::isSelected),
                festivalTime.getArtists()
                        .stream()
                        .map(UserTimetableFestivalArtistDTO::from)
                        .toList()
        );
    }
}