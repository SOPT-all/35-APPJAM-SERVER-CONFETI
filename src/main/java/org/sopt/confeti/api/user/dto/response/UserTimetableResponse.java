package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserTimetableDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record UserTimetableResponse(
        long typeId,
        String posterUrl,
        String title
) {
    public static UserTimetableResponse of(final UserTimetableDTO userTimetableDTO, S3FileHandler s3FileHandler) {
        return new UserTimetableResponse(
                userTimetableDTO.typeId(),
                s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER),
                        userTimetableDTO.posterPath()).toString(),
                userTimetableDTO.title()
        );
    }
}