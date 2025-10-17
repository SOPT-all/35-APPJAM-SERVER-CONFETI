package org.sopt.confeti.global.messagebroker.sqs.handler.song;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.applemusic.song.application.SongService;
import org.sopt.confeti.global.messagebroker.handler.CreateEventHandler;
import org.sopt.confeti.global.messagebroker.sqs.event.song.CreateSongEvent;
import org.sopt.confeti.global.messagebroker.sqs.handler.SqsEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateSongEventHandler implements CreateEventHandler<CreateSongEvent>,
    SqsEventHandler {

    private final SongService songService;

    @Override
    @Transactional
    public void handle(CreateSongEvent event) {
        if (!songService.isExistBySongId(event.songId())) {
            songService.create(event.toCreateDTO());
        }
    }

    @Override
    public Class<CreateSongEvent> getSupportedType() {
        return CreateSongEvent.class;
    }

}
