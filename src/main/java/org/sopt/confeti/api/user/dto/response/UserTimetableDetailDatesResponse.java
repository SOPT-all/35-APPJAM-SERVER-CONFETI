package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserTimetableDatesDTO;
import org.sopt.confeti.global.util.DateConvertor;

public record UserTimetableDetailDatesResponse(
        long festivalDateId,
        String festivalAt
) {
    public static UserTimetableDetailDatesResponse from(UserTimetableDatesDTO userTimetableDatesDTO) {
        return new UserTimetableDetailDatesResponse(
                userTimetableDatesDTO.festivalDateId(),
                DateConvertor.convertToDefaultFormat(userTimetableDatesDTO.festivalAt())
        );
    }
}
