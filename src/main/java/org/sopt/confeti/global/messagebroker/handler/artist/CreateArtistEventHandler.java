package org.sopt.confeti.global.messagebroker.handler.artist;


import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.applemusic.artist.application.ArtistService;
import org.sopt.confeti.domain.applemusic.artist.event.CreateArtistEvent;
import org.sopt.confeti.global.messagebroker.handler.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateArtistEventHandler implements EventHandler<CreateArtistEvent> {

    private final ArtistService artistService;

    @Override
    @Transactional
    public void handle(CreateArtistEvent event) {
        if (!artistService.isExistByArtistId(event.artistId())) {
            artistService.create(event.toArtist());
        }
    }

    @Override
    public Class<CreateArtistEvent> getSupportedEventType() {
        return CreateArtistEvent.class;
    }

    @Override
    public String getSupportedTypeId() {
        return CreateArtistEvent.class.getName();
    }
}
