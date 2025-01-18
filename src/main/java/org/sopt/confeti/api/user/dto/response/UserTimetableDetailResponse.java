package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserTimetableDTO;

import java.util.List;

public record UserTimetableDetailResponse(
        List<UserTimetableDetailFestivalResponse> festivals
) {
    public static UserTimetableDetailResponse from(UserTimetableDTO userTimeTableDTO) {
        return new UserTimetableDetailResponse(
                userTimeTableDTO.festivals().stream()
                        .map(UserTimetableDetailFestivalResponse::from)
                        .toList()
        );
    }
}
