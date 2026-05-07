package org.sopt.confeti.api.user.facade.dto.response.timetable;

import java.time.LocalDate;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.FestivalFileInfo;
import org.sopt.confeti.domain.timetable.Timetable;

public record TimetableCursorItemDTO(
    long timetableId,
    String posterUrl,
    String title,
    LocalDate startAt
) {

    public static TimetableCursorItemDTO of(Timetable timetable, FestivalFileInfo fileInfo) {
        Festival festival = timetable.getFestival();
        return new TimetableCursorItemDTO(
            timetable.getId(),
            fileInfo.posterUrl(),
            festival.getTitle(),
            festival.getStartAt()
        );
    }
}
