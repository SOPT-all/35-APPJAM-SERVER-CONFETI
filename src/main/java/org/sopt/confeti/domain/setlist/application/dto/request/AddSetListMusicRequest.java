package org.sopt.confeti.domain.setlist.application.dto.request;

public record AddSetListMusicRequest(
        String artistName,
        String trackName,
        String artworkUrl,
        String previewUrl
) {
}
