package org.sopt.confeti.api.user.facade.dto.request.timetable;

import org.sopt.confeti.api.user.dto.request.timetable.AddTimetableArtistRequest;

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
