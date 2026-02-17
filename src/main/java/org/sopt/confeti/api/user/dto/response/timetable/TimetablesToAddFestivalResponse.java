package org.sopt.confeti.api.user.dto.response.timetable;

import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableToAddDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record TimetablesToAddFestivalResponse(
    long festivalId,
    String posterUrl,
    String title
) {

    public static TimetablesToAddFestivalResponse of(final TimetableToAddDTO timetableToAddDTO,
        final S3FileHandler s3FileHandler) {
        return new TimetablesToAddFestivalResponse(
            timetableToAddDTO.festivalId(),
            s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER),
                timetableToAddDTO.posterPath()).toString(),
            timetableToAddDTO.title()
        );
    }
}
