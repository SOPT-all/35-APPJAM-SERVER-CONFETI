package org.sopt.confeti.api.user.dto.request;

import java.util.Set;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimetableDTO;

/**
 * 타임 테이블 삭제를 위함.
 */
public record PatchTimetableFestivalRequest(
    Set<Long> deleteTimetableFestivalIds
) {

    public PatchTimetableDTO toDTO() {
        return new PatchTimetableDTO(deleteTimetableFestivalIds);
    }
}
