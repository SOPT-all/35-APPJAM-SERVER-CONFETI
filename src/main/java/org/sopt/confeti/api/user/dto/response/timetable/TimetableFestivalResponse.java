package org.sopt.confeti.api.user.dto.response.timetable;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableFestivalBasicDTO;
import org.sopt.confeti.global.util.DateConvertor;

public record TimetableFestivalResponse(
    String ticketOpenAt,
    int stageCount,
    List<TimetableFestivalStageResponse> stages
) {

    public static TimetableFestivalResponse from(TimetableFestivalBasicDTO timetableFestival) {
        return new TimetableFestivalResponse(
            DateConvertor.convertToDefaultFormat(timetableFestival.festivalDate(),
                timetableFestival.ticketOpenAt()),
            timetableFestival.stages().size(),
            timetableFestival.stages().stream()
                .map(stage -> TimetableFestivalStageResponse.of(timetableFestival.festivalDate(),
                    stage))
                .toList()
        );
    }
}
