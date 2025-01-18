package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.festivaldate.FestivalDate;
import java.time.LocalDate;

public record UserTimetableDatesDTO(long festivalDateId, LocalDate festivalAt) {
    public static UserTimetableDatesDTO from(FestivalDate festivalDate) {
        return new UserTimetableDatesDTO(
                festivalDate.getId(),
                festivalDate.getFestivalAt()
        );
    }
}
