package org.sopt.confeti.domain.music.artist.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.domain.music.artist.CreateArtistsEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArtistEventListener {

    private final ArtistService artistService;

    @Async
    @EventListener
    public void handleCreateArtistsEvent(CreateArtistsEvent event) {
        try {
            artistService.create(event.artists());
            log.info("ArtistEventListener.handleCreateArtistsEvent : 아티스트 저장 완료. size : {}", event.artists().size());
        } catch (Exception e) {
            log.error("ArtistEventListener.handleCreateArtistsEvent : 아티스트 저장 실패. size : {}", event.artists().size(), e);
        }
    }
}
