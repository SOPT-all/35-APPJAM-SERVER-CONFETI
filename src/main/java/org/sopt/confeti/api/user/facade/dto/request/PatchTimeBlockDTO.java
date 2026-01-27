package org.sopt.confeti.api.user.facade.dto.request;

import java.util.List;
import org.sopt.confeti.api.user.dto.request.PatchTimetableRequest;

public record PatchTimeBlockDTO(
        List<PatchTimeBlockListDTO> timeBlocks
) {
    public static PatchTimeBlockDTO from(PatchTimetableRequest timetableRequest) {
        return new PatchTimeBlockDTO(
                timetableRequest.userTimetables()
                        .stream()
                        .map(PatchTimeBlockListDTO::from)
                        .toList()
        );
    }
}

