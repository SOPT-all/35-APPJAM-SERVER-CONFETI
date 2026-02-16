package org.sopt.confeti.api.user.facade.dto.response.timetable;

import java.time.LocalDate;
import org.sopt.confeti.domain.festival_date.FestivalDate;

public record TimetableFestivalDateDTO(long festivalDateId, LocalDate festivalAt) {

    public static TimetableFestivalDateDTO from(FestivalDate festivalDate) {
        return new TimetableFestivalDateDTO(
            festivalDate.getId(),
            festivalDate.getFestivalAt()
        );
    }
}
