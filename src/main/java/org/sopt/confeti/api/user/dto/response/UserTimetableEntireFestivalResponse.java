package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserTimetableEntireFestivalDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.DateConvertor;
import org.sopt.confeti.global.util.S3FileHandler;

import java.util.List;

public record UserTimetableEntireFestivalResponse(
        String logoUrl,
        String title,
        String startAt,
        String endAt,
        String area,
        List<UserTimetableDetailDatesResponse> festivalDates
) {
    public static UserTimetableEntireFestivalResponse of(UserTimetableEntireFestivalDTO userTimetableEntireFestivalDTO, S3FileHandler s3FileHandler) {
        return new UserTimetableEntireFestivalResponse(
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.FESTIVAL, FolderPath.LOGO),
                        userTimetableEntireFestivalDTO.logoPath()).toString(),
                userTimetableEntireFestivalDTO.title(),
                DateConvertor.convertToDefaultFormat(userTimetableEntireFestivalDTO.startAt()),
                DateConvertor.convertToDefaultFormat(userTimetableEntireFestivalDTO.endAt()),
                userTimetableEntireFestivalDTO.area(),
                userTimetableEntireFestivalDTO.festivalDates().stream()
                        .map(UserTimetableDetailDatesResponse::from)
                        .toList()
        );
    }
}
