package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.TimetableToAddDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record TimetablesToAddFestivalResponse(
        long performanceId,
        String posterUrl,
        String title
) {
    public static TimetablesToAddFestivalResponse from(TimetableToAddDTO timetableToAddDTO) {
        return new TimetablesToAddFestivalResponse(
                timetableToAddDTO.performanceId(),
                timetableToAddDTO.posterUrl(),
                timetableToAddDTO.title()
        );
    }
}
