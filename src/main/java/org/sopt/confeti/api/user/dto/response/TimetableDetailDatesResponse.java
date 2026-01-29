package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.TimetableFestivalDateDTO;
import org.sopt.confeti.global.util.DateConvertor;

public record TimetableDetailDatesResponse(
        long festivalDateId,
        String festivalAt
) {
    public static TimetableDetailDatesResponse from(TimetableFestivalDateDTO timetableFestivalDateDTO) {
        return new TimetableDetailDatesResponse(
                timetableFestivalDateDTO.festivalDateId(),
                DateConvertor.convertToDefaultFormat(timetableFestivalDateDTO.festivalAt())
        );
    }
}
