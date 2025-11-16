package org.sopt.confeti.global.messagebroker.sqs.handler.song;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.applemusic.song.application.SongService;
import org.sopt.confeti.global.messagebroker.handler.CreateMessageHandler;
import org.sopt.confeti.global.messagebroker.sqs.event.song.CreateSongMessage;
import org.sopt.confeti.global.messagebroker.sqs.handler.SqsMessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateSongMessageHandler implements CreateMessageHandler<CreateSongMessage>,
    SqsMessageHandler {

    private final SongService songService;

    @Override
    @Transactional
    public void handle(CreateSongMessage event) {
        if (!songService.isExistBySongId(event.songId())) {
            songService.create(event.toCreateDTO());
        }
    }

    @Override
    public Class<CreateSongMessage> getSupportedType() {
        return CreateSongMessage.class;
    }

}
