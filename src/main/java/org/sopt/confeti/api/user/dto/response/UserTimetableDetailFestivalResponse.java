package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.UserTimetableDetailFestivalDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record UserTimetableDetailFestivalResponse(
        long festivalId,
        String title,
        String logoUrl,
        List<UserTimetableDetailDatesResponse> festivalDates
) {
    public static UserTimetableDetailFestivalResponse of(UserTimetableDetailFestivalDTO userTimetableDetailFestivalDTO,
                                                         S3FileHandler s3FileHandler) {
        return new UserTimetableDetailFestivalResponse(
                userTimetableDetailFestivalDTO.festivalId(),
                userTimetableDetailFestivalDTO.title(),
                s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.LOGO),
                        userTimetableDetailFestivalDTO.logoPath()).toString(),
                userTimetableDetailFestivalDTO.festivalDates().stream()
                        .map(UserTimetableDetailDatesResponse::from)
                        .toList()
        );
    }
}
