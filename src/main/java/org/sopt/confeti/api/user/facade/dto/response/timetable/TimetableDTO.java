package org.sopt.confeti.api.user.facade.dto.response.timetable;

import org.sopt.confeti.domain.festival.FestivalFileInfo;
import org.sopt.confeti.domain.timetable.Timetable;

public record TimetableDTO(
    Long typeId,
    String posterUrl,
    String title
) {

    public static TimetableDTO of(final Timetable timetable, final FestivalFileInfo fileInfo) {
        return new TimetableDTO(
            timetable.getFestival().getId(),
            fileInfo.posterUrl(),
            timetable.getFestival().getTitle()
        );
    }
}
