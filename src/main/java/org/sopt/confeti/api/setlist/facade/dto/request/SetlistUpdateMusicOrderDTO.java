package org.sopt.confeti.api.setlist.facade.dto.request;

public record SetlistUpdateMusicOrderDTO(
        String musicId,
        int orders
) {
}
