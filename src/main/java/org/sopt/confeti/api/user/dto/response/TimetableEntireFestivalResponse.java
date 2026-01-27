package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.TimetableEntireFestivalDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.DateConvertor;
import org.sopt.confeti.global.util.S3FileHandler;

import java.util.List;

public record TimetableEntireFestivalResponse(
        String logoUrl,
        String title,
        String startAt,
        String endAt,
        String area,
        List<TimetableDetailDatesResponse> festivalDates
) {
    public static TimetableEntireFestivalResponse of(TimetableEntireFestivalDTO timetableEntireFestivalDTO, S3FileHandler s3FileHandler) {
        return new TimetableEntireFestivalResponse(
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.FESTIVAL, FolderPath.LOGO),
                        timetableEntireFestivalDTO.logoPath()).toString(),
                timetableEntireFestivalDTO.title(),
                DateConvertor.convertToDefaultFormat(timetableEntireFestivalDTO.startAt()),
                DateConvertor.convertToDefaultFormat(timetableEntireFestivalDTO.endAt()),
                timetableEntireFestivalDTO.area(),
                timetableEntireFestivalDTO.festivalDates().stream()
                        .map(TimetableDetailDatesResponse::from)
                        .toList()
        );
    }
}
