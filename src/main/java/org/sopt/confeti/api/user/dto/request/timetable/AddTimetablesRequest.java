package org.sopt.confeti.api.user.dto.request.timetable;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public record AddTimetablesRequest(
    @NotNull
    List<AddTimetableArtistRequest> festivals
) {

}
