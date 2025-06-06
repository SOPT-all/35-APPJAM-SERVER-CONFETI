package org.sopt.confeti.api.setlist.dto.response.search;

import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchMusicDTO;

public record SetlistSearchMusicResponse(
        String musicId,
        String trackName,
        String artistName,
        String artworkUrl,
        String previewUrl
) {
    public static SetlistSearchMusicResponse from(SetlistSearchMusicDTO artistMusic) {
        return new SetlistSearchMusicResponse(
                artistMusic.id(),
                artistMusic.trackName(),
                artistMusic.artistName(),
                artistMusic.artworkUrl(),
                artistMusic.previewUrl()
        );
    }
}
