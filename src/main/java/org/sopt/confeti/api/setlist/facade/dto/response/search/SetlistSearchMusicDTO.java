package org.sopt.confeti.api.setlist.facade.dto.response.search;

import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;

public record SetlistSearchMusicDTO(
        String id,
        String title,
        String artistName,
        String artworkUrl,
        String previewUrl
) {
    public static SetlistSearchMusicDTO from(ConfetiMusic music) {
        return new SetlistSearchMusicDTO(
                music.getId(),
                music.getTitle(),
                music.getArtistName(),
                music.getArtworkUrl(),
                music.getPreviewUrl()
        );
    }
}
