package org.sopt.confeti.api.user.facade.dto.request;

import java.util.List;
import org.sopt.confeti.api.user.dto.request.AddTimetableRequest;

public record AddTimetableDTO(
        List<AddTimetableArtistDTO> festivals
) {
    public static AddTimetableDTO from(final AddTimetableRequest addTimetableRequest) {
        return new AddTimetableDTO(
                addTimetableRequest.festivals().stream()
                        .map(AddTimetableArtistDTO::from)
                        .toList()
        );
    }
}
