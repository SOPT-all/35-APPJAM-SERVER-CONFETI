package org.sopt.confeti.global.messagebroker;

import org.sopt.confeti.domain.applemusic.artist.event.CreateArtistEvent;
import org.sopt.confeti.domain.applemusic.song.event.CreateSongEvent;

public interface MessageBroker {

    void sendCreateArtistEvent(CreateArtistEvent event);

    void sendCreateSongEvent(CreateSongEvent event);
}
