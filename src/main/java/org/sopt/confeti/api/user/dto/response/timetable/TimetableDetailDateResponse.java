package org.sopt.confeti.api.user.dto.response.timetable;

import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableFestivalDateDTO;
import org.sopt.confeti.global.util.DateConvertor;

public record TimetableDetailDateResponse(
    long festivalDateId,
    String festivalAt
) {

    public static TimetableDetailDateResponse from(
        TimetableFestivalDateDTO timetableFestivalDateDTO) {
        return new TimetableDetailDateResponse(
            timetableFestivalDateDTO.festivalDateId(),
            DateConvertor.convertToDefaultFormat(timetableFestivalDateDTO.festivalAt())
        );
    }
}
