package org.sopt.confeti.api.setlist.facade.dto.request;

public record SetlistAddMusicDTO(
        String musicId,
        String artistName,
        String trackName,
        String artworkUrl,
        String previewUrl
) {
}
