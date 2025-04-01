package org.sopt.confeti.api.user.facade.dto.request;

import java.util.List;
import org.sopt.confeti.api.user.dto.request.PatchTimetableRequest;

public record PatchTimetableDTO(
        List<PatchTimetableListDTO> userTimetables
) {
    public static PatchTimetableDTO from(PatchTimetableRequest timetableRequest) {
        return new PatchTimetableDTO(
                timetableRequest.userTimetables()
                        .stream()
                        .map(PatchTimetableListDTO::from)
                        .toList()
        );
    }
}

