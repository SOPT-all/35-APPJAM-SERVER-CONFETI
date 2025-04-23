package org.sopt.confeti.domain.setlist.application.dto.request;

import org.sopt.confeti.domain.setlist.SetlistType;

public record SetlistCreateRequest(
        SetlistType type,
        Long typeId
) {
}
