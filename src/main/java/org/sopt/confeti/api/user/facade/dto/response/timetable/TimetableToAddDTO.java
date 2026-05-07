package org.sopt.confeti.api.user.facade.dto.response.timetable;

import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.FestivalFileInfo;

public record TimetableToAddDTO(
    long festivalId,
    String posterUrl,
    String title
) {

    public static TimetableToAddDTO of(final Festival festival, final FestivalFileInfo fileInfo) {
        return new TimetableToAddDTO(
            festival.getId(),
            fileInfo.posterUrl(),
            festival.getTitle()
        );
    }
}
