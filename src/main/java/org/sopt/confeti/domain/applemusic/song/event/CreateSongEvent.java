package org.sopt.confeti.domain.applemusic.song.event;


import org.sopt.confeti.domain.applemusic.song.Song;
import org.sopt.confeti.global.messagebroker.message.CreateEvent;

public record CreateSongEvent(
    String songId,
    String name,
    String artworkUrl,
    Integer artworkWidth,
    Integer artworkHeight
) implements CreateEvent {

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
