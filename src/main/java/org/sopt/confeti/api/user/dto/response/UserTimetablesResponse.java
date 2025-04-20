package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserTimetablesDTO;
import org.sopt.confeti.global.util.S3FileHandler;

import java.util.List;

public record UserTimetablesResponse(
        int timetableCount,
        List<UserTimetableResponse> timetables
) {
    public static UserTimetablesResponse of(final UserTimetablesDTO timetableDTO, S3FileHandler s3FileHandler) {
        return new UserTimetablesResponse(
                timetableDTO.timetables().size(),
                timetableDTO.timetables().stream()
                        .map(userTimetable -> UserTimetableResponse.of(userTimetable, s3FileHandler))
                        .toList()
        );
    }
}