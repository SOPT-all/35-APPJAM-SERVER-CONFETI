package org.sopt.confeti.api.user.dto.request.timetable;

import java.util.List;

public record AddTimetablesRequest(
    List<AddTimetableArtistRequest> festivals
) {

}
