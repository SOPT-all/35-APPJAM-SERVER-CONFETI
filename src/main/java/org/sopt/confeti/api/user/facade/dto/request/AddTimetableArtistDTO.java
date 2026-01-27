package org.sopt.confeti.api.user.facade.dto.request;

import org.sopt.confeti.api.user.dto.request.AddTimetableArtistRequest;

public record AddTimetableArtistDTO(
        long festivalId
) {
    public static AddTimetableArtistDTO from(
            final AddTimetableArtistRequest addTimetableArtistRequest) {
        return new AddTimetableArtistDTO(
                addTimetableArtistRequest.festivalId()
        );
    }
}
