package org.sopt.confeti.domain.applemusic.song.application.dto.request;

import lombok.Builder;
import org.sopt.confeti.domain.applemusic.song.Song;

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
