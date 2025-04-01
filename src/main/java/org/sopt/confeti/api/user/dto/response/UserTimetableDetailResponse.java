package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record UserTimetableDetailResponse(
        List<UserTimetableDetailFestivalResponse> festivals
) {
    public static UserTimetableDetailResponse of(UserTimetableDTO userTimeTableDTO, S3FileHandler s3FileHandler) {
        return new UserTimetableDetailResponse(
                userTimeTableDTO.festivals().stream()
                        .map(timetableFestivalDTO -> UserTimetableDetailFestivalResponse.of(timetableFestivalDTO,
                                s3FileHandler))
                        .toList()
        );
    }
}
