package org.sopt.confeti.domain.applemusic.artist.event;


import org.sopt.confeti.domain.applemusic.artist.Artist;
import org.sopt.confeti.global.messagebroker.message.CreateEvent;

public record CreateArtistEvent(
    String artistId,
    String name,
    String artworkUrl,
    Integer artworkWidth,
    Integer artworkHeight
) implements CreateEvent {

    public Artist toArtist() {
        return Artist.builder()
            .artistId(artistId)
            .name(name)
            .artworkUrl(artworkUrl)
            .artworkWidth(artworkWidth)
            .artworkHeight(artworkHeight)
            .build();
    }
}
