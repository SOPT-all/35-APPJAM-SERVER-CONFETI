package org.sopt.confeti.api.setlist.facade.dto.request;

public record SetlistAddSongDTO(
    String songId,
    String artistName,
    String trackName,
    String artworkUrl,
    String previewUrl
) {

}
