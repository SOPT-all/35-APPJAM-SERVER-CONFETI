package org.sopt.confeti.api.user.dto.request.timetable;

import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.sopt.confeti.api.user.facade.dto.request.timetable.PatchTimetablesCommand;

/**
 * 타임 테이블 삭제를 위함.
 */
public record PatchTimetablesRequest(
    @NotNull
    Set<Long> deleteTimetableIds
) {

    public PatchTimetablesCommand toCommand() {
        return new PatchTimetablesCommand(deleteTimetableIds);
    }
}
