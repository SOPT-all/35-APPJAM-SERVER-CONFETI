package org.sopt.confeti.api.user.dto.response.timetable;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableEntireFestivalDTO;
import org.sopt.confeti.global.util.DateConvertor;

public record TimetableEntireFestivalResponse(
    String logoUrl,
    String title,
    String startAt,
    String endAt,
    String area,
    List<TimetableDetailDateResponse> festivalDates
) {

    public static TimetableEntireFestivalResponse from(
        TimetableEntireFestivalDTO timetableEntireFestivalDTO) {
        return new TimetableEntireFestivalResponse(
            timetableEntireFestivalDTO.logoUrl(),
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
