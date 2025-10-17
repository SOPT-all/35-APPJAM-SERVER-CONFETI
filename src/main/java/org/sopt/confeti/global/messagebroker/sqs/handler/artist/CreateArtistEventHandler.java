package org.sopt.confeti.global.messagebroker.sqs.handler.artist;


import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.applemusic.artist.application.ArtistService;
import org.sopt.confeti.global.messagebroker.handler.CreateEventHandler;
import org.sopt.confeti.global.messagebroker.sqs.event.artist.CreateArtistEvent;
import org.sopt.confeti.global.messagebroker.sqs.handler.SqsEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateArtistEventHandler implements CreateEventHandler<CreateArtistEvent>,
    SqsEventHandler {

    private final ArtistService artistService;

    @Override
    @Transactional
    public void handle(CreateArtistEvent event) {
        if (!artistService.isExistByArtistId(event.artistId())) {
            artistService.create(event.toArtist());
        }
    }

    @Override
    public Class<CreateArtistEvent> getSupportedType() {
        return CreateArtistEvent.class;
    }

}
