package org.sopt.confeti.api.setlist.facade.dto.response.search;

import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;

public record SetlistSearchArtistMusicDTO(
        String id,
        String trackName,
        String artistName,
        String artworkUrl,
        String previewUrl
) {
    public static SetlistSearchArtistMusicDTO from(ConfetiMusic music) {
        return new SetlistSearchArtistMusicDTO(
                music.getId(),
                music.getTrackName(),
                music.getArtistName(),
                music.getArtworkUrl(),
                music.getPreviewUrl()
        );
    }
}
