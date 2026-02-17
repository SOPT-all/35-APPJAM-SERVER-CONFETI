package org.sopt.confeti.api.user.facade.dto.request.timetable;

import java.util.Set;

public record PatchTimetablesCommand(
    Set<Long> deleteTimetableIds
) {

}
