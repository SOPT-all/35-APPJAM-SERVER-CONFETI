package org.sopt.confeti.global.messagebroker.sqs.handler.artist;


import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.music.artist.application.ArtistService;
import org.sopt.confeti.global.messagebroker.handler.CreateMessageHandler;
import org.sopt.confeti.global.messagebroker.sqs.handler.SqsMessageHandler;
import org.sopt.confeti.global.messagebroker.sqs.message.artist.CreateArtistMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateArtistMessageHandler implements CreateMessageHandler<CreateArtistMessage>,
    SqsMessageHandler {

    private final ArtistService artistService;

    @Override
    @Transactional
    public void handle(CreateArtistMessage message) {
        if (!artistService.isExistByArtistId(message.artistId())) {
            artistService.create(message.toCreateDTO());
        }
    }

    @Override
    public Class<CreateArtistMessage> getSupportedType() {
        return CreateArtistMessage.class;
    }

}
