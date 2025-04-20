package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableDetailFestivalsDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record UserTimetableDetailFestivalsResponse(
        List<UserTimetableDetailFestivalResponse> festivals
) {
    public static UserTimetableDetailFestivalsResponse of(UserTimetableDetailFestivalsDTO userTimeTableDetailFestivalsDTO, S3FileHandler s3FileHandler) {
        return new UserTimetableDetailFestivalsResponse(
                userTimeTableDetailFestivalsDTO.festivals().stream()
                        .map(timetableFestivalDTO -> UserTimetableDetailFestivalResponse.of(timetableFestivalDTO,
                                s3FileHandler))
                        .toList()
        );
    }
}
