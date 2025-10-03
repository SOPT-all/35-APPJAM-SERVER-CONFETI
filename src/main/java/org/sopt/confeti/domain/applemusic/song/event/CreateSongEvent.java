package org.sopt.confeti.domain.applemusic.song.event;


import org.sopt.confeti.domain.applemusic.song.Song;
import org.sopt.confeti.global.messagebroker.Event;

public record CreateSongEvent(
    String songId,
    String name,
    String artworkUrl,
    Integer artworkWidth,
    Integer artworkHeight
) implements Event {

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
