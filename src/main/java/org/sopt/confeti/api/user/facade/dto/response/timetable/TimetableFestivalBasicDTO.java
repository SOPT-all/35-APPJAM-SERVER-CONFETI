package org.sopt.confeti.api.user.facade.dto.response.timetable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.time_block.TimeBlock;

public record TimetableFestivalBasicDTO(
    LocalDate festivalDate,
    LocalTime ticketOpenAt,
    List<TimetableFestivalStageDTO> stages
) {

    public static TimetableFestivalBasicDTO of(FestivalDate festivalDate,
        Map<Long, TimeBlock> timeBlocks) {
        return new TimetableFestivalBasicDTO(
            festivalDate.getFestivalAt(),
            festivalDate.getOpenAt(),
            festivalDate.getStages()
                .stream()
                .map(stages -> TimetableFestivalStageDTO.of(stages, timeBlocks))
                .toList()
        );
    }
}
