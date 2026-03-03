package org.sopt.confeti.global.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3EventListener {

    private final S3FileHandler s3FileHandler;

    @Async
    @EventListener
    public void handleDeleteEvent(S3FileDeleteEvent event) {
        try {
            s3FileHandler.deleteFile(event.folderPath(), event.filePath());
            log.info("S3EventListener.handleDeleteEvent : 기존 S3 파일 삭제 완료. path : {}", event.filePath());
        } catch (Exception e) {
            log.error("S3EventListener.handleDeleteEvent : 기존 파일 삭제 실패 (S3 고아 객체 발생). path : {}", event.filePath(), e);
        }
    }
}
