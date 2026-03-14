package org.sopt.confeti.domain.music.relatedartist.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.domain.music.artist.application.ArtistService;
import org.sopt.confeti.domain.music.relatedartist.CreateRelatedArtistsEvent;
import org.sopt.confeti.global.common.ExecutorName;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.transaction.Tx;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RelatedArtistEventListener {

    private final ArtistService artistService;
    private final RelatedArtistService relatedArtistService;

    @Async(ExecutorName.MUSIC_API_EVENT_EXECUTOR)
    @EventListener
    public void handleCreateRelatedArtistsEvent(CreateRelatedArtistsEvent event) {
        try {
            Tx.masterTx(() -> {
                List<ConfetiArtist> allArtists = new ArrayList<>();
                allArtists.add(event.artist());
                allArtists.addAll(event.relatedArtists());
                artistService.create(allArtists);

                Set<String> relatedArtistIds = event.relatedArtists().stream()
                    .map(ConfetiArtist::getId)
                    .collect(Collectors.toSet());
                relatedArtistService.createRelatedArtists(event.artist().getId(), relatedArtistIds);
            });

            log.info(
                "RelatedArtistEventListener.handleCreateRelatedArtistsEvent : 관련 아티스트 저장 완료. artistId : {}",
                event.artist().getId());
        } catch (Exception e) {
            log.error(
                "RelatedArtistEventListener.handleCreateRelatedArtistsEvent : 관련 아티스트 저장 실패. artistId : {}",
                event.artist().getId(), e);
        }
    }
}
