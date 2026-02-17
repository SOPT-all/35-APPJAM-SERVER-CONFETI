package org.sopt.confeti.api.user.facade.dto.request.timetable;

import java.util.List;
import org.sopt.confeti.api.user.dto.request.timetable.PatchTimeBlocksRequest;

public record PatchTimeBlocksDTO(
    List<PatchTimeBlockDTO> timeBlocks
) {

    public static PatchTimeBlocksDTO from(PatchTimeBlocksRequest timetableRequest) {
        return new PatchTimeBlocksDTO(
            timetableRequest.timeBlocks()
                .stream()
                .map(PatchTimeBlockDTO::from)
                .toList()
        );
    }
}
