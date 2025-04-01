package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableFestivalDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record UserTimetableDetailFestivalResponse(
        long festivalId,
        String title,
        String logoUrl,
        List<UserTimetableDetailDatesResponse> festivalDates
) {
    public static UserTimetableDetailFestivalResponse of(UserTimetableFestivalDTO userTimetableFestivalDTO,
                                                         S3FileHandler s3FileHandler) {
        return new UserTimetableDetailFestivalResponse(
                userTimetableFestivalDTO.festivalId(),
                userTimetableFestivalDTO.title(),
                s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.LOGO),
                        userTimetableFestivalDTO.logoPath()).toString(),
                userTimetableFestivalDTO.festivalDates().stream()
                        .map(UserTimetableDetailDatesResponse::from)
                        .toList()
        );
    }
}
