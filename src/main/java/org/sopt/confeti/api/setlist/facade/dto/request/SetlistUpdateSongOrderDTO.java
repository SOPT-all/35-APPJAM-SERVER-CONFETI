package org.sopt.confeti.api.setlist.facade.dto.request;

public record SetlistUpdateSongOrderDTO(
    String songId,
    int orders
) {

}
