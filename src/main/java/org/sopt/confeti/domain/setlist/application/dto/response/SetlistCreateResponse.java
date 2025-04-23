package org.sopt.confeti.domain.setlist.application.dto.response;

import java.util.List;

public record SetlistCreateResponse(
        List<Long> setlistIds
) {
}
