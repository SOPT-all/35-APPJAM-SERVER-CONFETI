package org.sopt.confeti.domain.music.song.application.dto.request;

import lombok.Builder;
import org.sopt.confeti.domain.music.song.Song;

@Builder
public record CreateSongDTO(
    String songId,
    String name,
    String artworkUrl
) {

    public Song toSong() {
        return Song.builder()
            .songId(songId)
            .trackName(name)
            .artworkUrl(artworkUrl)
            .build();
    }

}
