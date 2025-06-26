package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserTimetablePastFestivalDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.DateConvertor;
import org.sopt.confeti.global.util.S3FileHandler;

import java.util.List;

public record UserTimetablePastFestivalResponse(
        String logoUrl,
        String title,
        String startAt,
        String endAt,
        String area,
        List<UserTimetableDetailDatesResponse> festivalDates
) {
    public static UserTimetablePastFestivalResponse of(UserTimetablePastFestivalDTO userTimetablePastFestivalDTO, S3FileHandler s3FileHandler) {
        return new UserTimetablePastFestivalResponse(
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.FESTIVAL, FolderPath.LOGO),
                        userTimetablePastFestivalDTO.logoPath()).toString(),
                userTimetablePastFestivalDTO.title(),
                DateConvertor.convertToDefaultFormat(userTimetablePastFestivalDTO.startAt()),
                DateConvertor.convertToDefaultFormat(userTimetablePastFestivalDTO.endAt()),
                userTimetablePastFestivalDTO.area(),
                userTimetablePastFestivalDTO.festivalDates().stream()
                        .map(UserTimetableDetailDatesResponse::from)
                        .toList()
        );
    }
}
