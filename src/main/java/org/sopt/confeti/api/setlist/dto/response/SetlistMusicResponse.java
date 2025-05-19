package org.sopt.confeti.api.setlist.dto.response;

public record SetlistMusicResponse(
        Long musicId,
        String trackId,
        String artistName,
        String trackName,
        String artworkUrl,
        String previewUrl,
        int orders
) {
}
