package org.sopt.confeti.api.user.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableFestivalTimeDTO;
import org.sopt.confeti.global.util.DateConvertor;

import java.util.List;

public record UserTimetableFestivalTimeResponse(
        Long userTimetableId,
        String startAt,
        String endAt,
        Boolean isSelected,
        List<UserTimetableFestivalArtistResponse> artists
) {
    public static UserTimetableFestivalTimeResponse of(LocalDate festivalDate, UserTimetableFestivalTimeDTO festivalTime) {
        return new UserTimetableFestivalTimeResponse(
                festivalTime.userTimetableId(),
                DateConvertor.convert(festivalDate, festivalTime.startAt()),
                DateConvertor.convert(festivalDate, festivalTime.endAt()),
                festivalTime.isSelected(),
                festivalTime.artists().stream()
                        .map(UserTimetableFestivalArtistResponse::from)
                        .toList()
        );
    }
}