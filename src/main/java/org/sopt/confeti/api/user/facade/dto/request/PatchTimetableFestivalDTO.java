package org.sopt.confeti.api.user.facade.dto.request;

import java.util.Set;

public record PatchTimetableFestivalDTO(
    Set<Long> deleteFestivalIds
) {

}
