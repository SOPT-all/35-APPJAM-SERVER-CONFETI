package org.sopt.confeti.domain.applemusic.song.application.dto.request;

import lombok.Builder;
import org.sopt.confeti.domain.applemusic.song.Song;

@Builder
public record CreateSongDTO(
    String songId,
    String name,
    String artworkUrl,
    Integer artworkWidth,
    Integer artworkHeight
) {

    public Song toSong() {
        return Song.builder()
            .songId(songId)
            .name(name)
            .artworkUrl(artworkUrl)
            .artworkWidth(artworkWidth)
            .artworkHeight(artworkHeight)
            .build();
    }

}
