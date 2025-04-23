package org.sopt.confeti.domain.setlist.application.dto.request;

public record SetlistMusicOrderUpdateRequest(
        String trackId,
        int orders
) {
}
