package org.sopt.confeti.global.messagebroker.handler.song;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.applemusic.song.application.SongService;
import org.sopt.confeti.domain.applemusic.song.event.CreateSongEvent;
import org.sopt.confeti.global.messagebroker.handler.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateSongEventHandler implements EventHandler<CreateSongEvent> {

    private final SongService songService;

    @Override
    public void handle(CreateSongEvent event) {
        if (!songService.isExistBySongId(event.songId())) {
            songService.create(event.toSong());
        }
    }

    @Override
    public Class<CreateSongEvent> getSupportedEventType() {
        return CreateSongEvent.class;
    }

    @Override
    public String getSupportedTypeId() {
        return CreateSongEvent.class.getName();
    }
}
