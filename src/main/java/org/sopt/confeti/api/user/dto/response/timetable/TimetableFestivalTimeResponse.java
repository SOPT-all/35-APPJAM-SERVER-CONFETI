package org.sopt.confeti.api.user.dto.response.timetable;

import java.time.LocalDate;
import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableFestivalTimeDTO;
import org.sopt.confeti.global.util.DateConvertor;

public record TimetableFestivalTimeResponse(
    Long timeBlockId,
    String name,
    String startAt,
    String endAt,
    Boolean isSelected,
    List<TimetableFestivalArtistResponse> artists
) {

    public static TimetableFestivalTimeResponse of(LocalDate festivalDate,
        TimetableFestivalTimeDTO festivalTime) {
        return new TimetableFestivalTimeResponse(
            festivalTime.timeBlockId(),
            festivalTime.name(),
            DateConvertor.convertToDefaultFormat(festivalDate, festivalTime.startAt()),
            DateConvertor.convertToDefaultFormat(festivalDate, festivalTime.endAt()),
            festivalTime.isSelected(),
            festivalTime.artists().stream()
                .map(TimetableFestivalArtistResponse::from)
                .toList()
        );
    }
}
