package org.sopt.confeti.api.user.facade.dto.response;

import java.util.List;
import java.util.Map;
import org.sopt.confeti.domain.festival_stage.FestivalStage;
import org.sopt.confeti.domain.time_block.TimeBlock;

public record TimetableFestivalStageDTO(
        int stageOrder,
        String stageName,
        List<TimetableFestivalTimeDTO> festivalTimes
) {
    public static TimetableFestivalStageDTO of(FestivalStage festivalStage, Map<Long, TimeBlock> timeBlocks
    ) {
        return new TimetableFestivalStageDTO(
                festivalStage.getOrder(),
                festivalStage.getName(),
                festivalStage.getTimes()
                        .stream()
                        .map(time -> TimetableFestivalTimeDTO.of(time, timeBlocks))
                        .toList()
        );
    }
}
