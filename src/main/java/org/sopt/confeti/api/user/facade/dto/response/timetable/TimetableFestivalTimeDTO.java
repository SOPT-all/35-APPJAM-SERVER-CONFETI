package org.sopt.confeti.api.user.facade.dto.response.timetable;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.sopt.confeti.domain.festival_time.FestivalTime;
import org.sopt.confeti.domain.time_block.TimeBlock;


public record TimetableFestivalTimeDTO(
    long timeBlockId,
    LocalTime startAt,
    LocalTime endAt,
    boolean isSelected,
    List<TimetableFestivalArtistDTO> artists
) {

    public static TimetableFestivalTimeDTO of(FestivalTime festivalTime,
        Map<Long, TimeBlock> timeBlocks) {
        return new TimetableFestivalTimeDTO(
            timeBlocks.get(festivalTime.getId()).getId(),
            festivalTime.getStartAt(),
            festivalTime.getEndAt(),
            timeBlocks.get(festivalTime.getId()).isSelected(),
            festivalTime.getArtists()
                .stream()
                .map(TimetableFestivalArtistDTO::from)
                .toList()
        );
    }
}
