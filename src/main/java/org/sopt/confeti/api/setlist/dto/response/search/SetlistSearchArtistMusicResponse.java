package org.sopt.confeti.api.setlist.dto.response.search;

import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchArtistMusicDTO;

public record SetlistSearchArtistMusicResponse(
        String musicId,
        String trackName,
        String artistName,
        String artworkUrl,
        String previewUrl
) {
    public static SetlistSearchArtistMusicResponse from(SetlistSearchArtistMusicDTO artistMusic) {
        return new SetlistSearchArtistMusicResponse(
                artistMusic.id(),
                artistMusic.trackName(),
                artistMusic.artistName(),
                artistMusic.artworkUrl(),
                artistMusic.previewUrl()
        );
    }
}
