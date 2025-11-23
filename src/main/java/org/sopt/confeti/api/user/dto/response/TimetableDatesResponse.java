package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.TimetableDatesDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record TimetableDatesResponse(
    long timetableFestivalId,
    String title,
    String posterUrl,
    List<TimetableDateResponse> dates
) {

    public static TimetableDatesResponse of(TimetableDatesDTO timetableDatesDTO,
        S3FileHandler s3FileHandler) {
        List<TimetableDateResponse> dates = timetableDatesDTO.dates().stream()
            .map(TimetableDateResponse::from)
            .toList();

        return new TimetableDatesResponse(
            timetableDatesDTO.timetableFestivalId(),
            timetableDatesDTO.title(),
            s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER),
                timetableDatesDTO.posterUrl()).toString(),
            dates
        );
    }
}
