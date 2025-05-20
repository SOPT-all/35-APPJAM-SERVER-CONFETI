package org.sopt.confeti.api.setlist.facade.dto.request;

import org.sopt.confeti.domain.setlist.SetlistType;

public record SetlistCreateRequestDTO(
        SetlistType type,
        Long typeId
) {
}
