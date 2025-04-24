package org.sopt.confeti.domain.setlist.application.dto.response;

public record SetlistMusicResponseDto(
        Long musicId,
        String trackId,
        String artistName,
        String trackName,
        String artworkUrl,
        String previewUrl,
        int orders
) {
}
