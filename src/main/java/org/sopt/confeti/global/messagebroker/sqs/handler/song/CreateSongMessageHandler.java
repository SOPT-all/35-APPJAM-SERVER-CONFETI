package org.sopt.confeti.global.messagebroker.sqs.handler.song;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.applemusic.song.application.SongService;
import org.sopt.confeti.global.messagebroker.handler.CreateMessageHandler;
import org.sopt.confeti.global.messagebroker.sqs.handler.SqsMessageHandler;
import org.sopt.confeti.global.messagebroker.sqs.message.song.CreateSongMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateSongMessageHandler implements CreateMessageHandler<CreateSongMessage>,
    SqsMessageHandler {

    private final SongService songService;

    @Override
    @Transactional
    public void handle(CreateSongMessage message) {
        if (!songService.isExistBySongId(message.songId())) {
            songService.create(message.toCreateDTO());
        }
    }

    @Override
    public Class<CreateSongMessage> getSupportedType() {
        return CreateSongMessage.class;
    }

}
