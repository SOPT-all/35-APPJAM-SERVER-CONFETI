package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.TimetableToAddDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record TimetablesToAddFestivalResponse(
        long festivalId,
        String posterUrl,
        String title
) {
    public static TimetablesToAddFestivalResponse of(final TimetableToAddDTO timetableToAddDTO, final S3FileHandler s3FileHandler) {
        return new TimetablesToAddFestivalResponse (
                timetableToAddDTO.festivalId(),
                s3FileHandler.getFileUrl(timetableToAddDTO.posterPath()),
                timetableToAddDTO.title()
        );
    }
}
