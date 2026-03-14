package org.sopt.confeti.domain.music.song.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.domain.music.song.CreateSongsEvent;
import org.sopt.confeti.global.common.ExecutorName;
import org.sopt.confeti.global.transaction.Tx;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SongEventListener {

    private final SongService songService;

    @Async(ExecutorName.MUSIC_API_EVENT_EXECUTOR)
    @EventListener
    public void handleCreateSongsEvent(CreateSongsEvent event) {
        try {
            Tx.masterTx(() -> songService.create(event.songs()));
            log.info("SongEventListener.handleCreateSongsEvent : 노래 저장 완료. size : {}", event.songs().size());
        } catch (Exception e) {
            log.error("SongEventListener.handleCreateSongsEvent : 노래 저장 실패. size : {}", event.songs().size(), e);
        }
    }
}
