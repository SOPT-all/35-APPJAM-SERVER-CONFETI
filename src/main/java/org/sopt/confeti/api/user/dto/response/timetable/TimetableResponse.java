package org.sopt.confeti.api.user.dto.response.timetable;

import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record TimetableResponse(
    long typeId,
    String posterUrl,
    String title
) {

    public static TimetableResponse of(final TimetableDTO timetableDTO,
        S3FileHandler s3FileHandler) {
        return new TimetableResponse(
            timetableDTO.typeId(),
            s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER),
                timetableDTO.posterPath()).toString(),
            timetableDTO.title()
        );
    }
}
