package org.sopt.confeti.global.messagebroker.sqs.handler.artist;


import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.applemusic.artist.application.ArtistService;
import org.sopt.confeti.global.messagebroker.handler.CreateMessageHandler;
import org.sopt.confeti.global.messagebroker.sqs.event.artist.CreateArtistMessage;
import org.sopt.confeti.global.messagebroker.sqs.handler.SqsMessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateArtistMessageHandler implements CreateMessageHandler<CreateArtistMessage>,
    SqsMessageHandler {

    private final ArtistService artistService;

    @Override
    @Transactional
    public void handle(CreateArtistMessage event) {
        if (!artistService.isExistByArtistId(event.artistId())) {
            artistService.create(event.toCreateDTO());
        }
    }

    @Override
    public Class<CreateArtistMessage> getSupportedType() {
        return CreateArtistMessage.class;
    }

}
