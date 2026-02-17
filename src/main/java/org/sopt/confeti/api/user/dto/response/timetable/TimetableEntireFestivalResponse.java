package org.sopt.confeti.api.user.dto.response.timetable;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableEntireFestivalDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.DateConvertor;
import org.sopt.confeti.global.util.S3FileHandler;

public record TimetableEntireFestivalResponse(
    String logoUrl,
    String title,
    String startAt,
    String endAt,
    String area,
    List<TimetableDetailDateResponse> festivalDates
) {

    public static TimetableEntireFestivalResponse of(
        TimetableEntireFestivalDTO timetableEntireFestivalDTO, S3FileHandler s3FileHandler) {
        return new TimetableEntireFestivalResponse(
            s3FileHandler.getFileUrl(
                FolderPath.combine(FolderPath.FESTIVAL, FolderPath.LOGO),
                timetableEntireFestivalDTO.logoPath()).toString(),
            timetableEntireFestivalDTO.title(),
            DateConvertor.convertToDefaultFormat(timetableEntireFestivalDTO.startAt()),
            DateConvertor.convertToDefaultFormat(timetableEntireFestivalDTO.endAt()),
            timetableEntireFestivalDTO.area(),
            timetableEntireFestivalDTO.festivalDates().stream()
                .map(TimetableDetailDateResponse::from)
                .toList()
        );
    }
}
