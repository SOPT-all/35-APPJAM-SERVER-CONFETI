package org.sopt.confeti.domain.applemusic.song.event;


import org.sopt.confeti.domain.applemusic.song.Song;

public record CreateSongEvent(
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
