package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.TimetableDetailFestivalDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record TimetableDetailFestivalResponse(
        long festivalId,
        String title,
        String logoUrl,
        List<TimetableDetailDatesResponse> festivalDates
) {
    public static TimetableDetailFestivalResponse of(TimetableDetailFestivalDTO timetableDetailFestivalDTO,
                                                         S3FileHandler s3FileHandler) {
        return new TimetableDetailFestivalResponse(
                timetableDetailFestivalDTO.festivalId(),
                timetableDetailFestivalDTO.title(),
                s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.LOGO),
                        timetableDetailFestivalDTO.logoPath()).toString(),
                timetableDetailFestivalDTO.festivalDates().stream()
                        .map(TimetableDetailDatesResponse::from)
                        .toList()
        );
    }
}
