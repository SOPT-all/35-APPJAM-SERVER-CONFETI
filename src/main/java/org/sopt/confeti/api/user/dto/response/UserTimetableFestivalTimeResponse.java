package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserTimetableFestivalTimeDTO;
import org.sopt.confeti.global.util.DateConvertor;

import java.util.List;

public record UserTimetableFestivalTimeResponse(
        Long festivalTimeId,
        String startAt,
        String endAt,
        Boolean isSelected,
        List<UserTimetableFestivalArtistResponse> artists
) {
    public static UserTimetableFestivalTimeResponse from(UserTimetableFestivalTimeDTO festivalTime) {
        return new UserTimetableFestivalTimeResponse(
                festivalTime.festivalTimeId(),
                DateConvertor.convert(festivalTime.startAt()),
                DateConvertor.convert(festivalTime.endAt()),
                festivalTime.isSelected(),
                festivalTime.artists().stream()
                        .map(UserTimetableFestivalArtistResponse::from)
                        .toList()
        );
    }
}