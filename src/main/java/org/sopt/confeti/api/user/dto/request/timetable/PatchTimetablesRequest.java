package org.sopt.confeti.api.user.dto.request.timetable;

import java.util.Set;
import org.sopt.confeti.api.user.facade.dto.request.timetable.PatchTimetablesDTO;

/**
 * 타임 테이블 삭제를 위함.
 */
public record PatchTimetablesRequest(
    Set<Long> deleteTimetableIds
) {

    public PatchTimetablesDTO toDTO() {
        return new PatchTimetablesDTO(deleteTimetableIds);
    }
}
