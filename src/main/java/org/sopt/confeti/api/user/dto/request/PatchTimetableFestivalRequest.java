package org.sopt.confeti.api.user.dto.request;

import java.util.Set;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimetableFestivalDTO;

public record PatchTimetableFestivalRequest(
    Set<Long> deleteFestivalIds
) {

    public PatchTimetableFestivalDTO toDTO() {
        return new PatchTimetableFestivalDTO(deleteFestivalIds);
    }
}
