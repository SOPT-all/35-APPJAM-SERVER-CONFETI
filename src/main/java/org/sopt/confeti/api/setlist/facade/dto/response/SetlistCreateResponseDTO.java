package org.sopt.confeti.api.setlist.facade.dto.response;

import java.util.List;

public record SetlistCreateResponseDTO(
        List<Long> setlistIds
) {
}
