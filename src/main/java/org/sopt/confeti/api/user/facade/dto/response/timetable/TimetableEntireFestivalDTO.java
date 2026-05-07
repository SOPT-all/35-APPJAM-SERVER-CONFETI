package org.sopt.confeti.api.user.facade.dto.response.timetable;

import java.time.LocalDate;
import java.util.List;
import org.sopt.confeti.domain.festival.FestivalFileInfo;
import org.sopt.confeti.domain.timetable.Timetable;

public record TimetableEntireFestivalDTO(
    String logoUrl,
    String title,
    LocalDate startAt,
    LocalDate endAt,
    String area,
    List<TimetableFestivalDateDTO> festivalDates
) {

    public static TimetableEntireFestivalDTO of(Timetable timetable, FestivalFileInfo fileInfo) {
        return new TimetableEntireFestivalDTO(
            fileInfo.logoUrl(),
            timetable.getFestival().getTitle(),
            timetable.getFestival().getStartAt(),
            timetable.getFestival().getEndAt(),
            timetable.getFestival().getArea(),
            timetable.getFestival().getDates().stream()
                .map(TimetableFestivalDateDTO::from)
                .toList()
        );
    }
}
