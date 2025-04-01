package org.sopt.confeti.api.user.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.festival_date.FestivalDate;

public record UserTimetableDatesDTO(long festivalDateId, LocalDate festivalAt) {
    public static UserTimetableDatesDTO from(FestivalDate festivalDate) {
        return new UserTimetableDatesDTO(
                festivalDate.getId(),
                festivalDate.getFestivalAt()
        );
    }
}
