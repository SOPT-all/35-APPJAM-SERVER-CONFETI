package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserTimetableDatesDTO;

import java.time.LocalDate;

public record UserTimetableDetailDatesResponse(
        long festivalDateId,
        LocalDate festivalAt
) {
    public static UserTimetableDetailDatesResponse from(UserTimetableDatesDTO userTimetableDatesDTO) {
        return new UserTimetableDetailDatesResponse(
                userTimetableDatesDTO.festivalDateId(),
                userTimetableDatesDTO.fesitvalAt()
        );
    }
}
