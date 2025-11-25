package org.sopt.confeti.api.user.dto.request;

import java.util.Set;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimetableFestivalDTO;

/**
 * 타임 테이블 삭제를 위함.
 * TODO 관련 네이밍 재정의
 */
public record PatchTimetableFestivalRequest(
    Set<Long> deleteTimetableFestivalIds
) {

    public PatchTimetableFestivalDTO toDTO() {
        return new PatchTimetableFestivalDTO(deleteTimetableFestivalIds);
    }
}
