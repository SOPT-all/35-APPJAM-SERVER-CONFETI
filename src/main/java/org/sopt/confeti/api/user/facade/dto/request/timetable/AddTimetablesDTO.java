package org.sopt.confeti.api.user.facade.dto.request.timetable;

import java.util.List;
import org.sopt.confeti.api.user.dto.request.timetable.AddTimetablesRequest;

public record AddTimetablesDTO(
    List<AddTimetableArtistDTO> festivals
) {

    public static AddTimetablesDTO from(final AddTimetablesRequest addTimetablesRequest) {
        return new AddTimetablesDTO(
            addTimetablesRequest.festivals().stream()
                .map(AddTimetableArtistDTO::from)
                .toList()
        );
    }
}
