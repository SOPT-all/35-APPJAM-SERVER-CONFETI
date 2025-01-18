package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserTimetableFestivalDTO;
import org.sopt.confeti.domain.usertimetable.UserTimetable;

import java.util.List;

public record UserTimetableDetailFestivalResponse(
        long festivalId,
        String title,
        String logoUrl,
        List<UserTimetableDetailDatesResponse> festivalDates
) {
    public static UserTimetableDetailFestivalResponse from(UserTimetableFestivalDTO userTimetableFestivalDTO) {
        return new UserTimetableDetailFestivalResponse(
                userTimetableFestivalDTO.festivalId(),
                userTimetableFestivalDTO.title(),
                userTimetableFestivalDTO.logoUrl(),
                userTimetableFestivalDTO.festivalDates().stream()
                        .map(UserTimetableDetailDatesResponse::from)
                        .toList()
        );
    }
}
