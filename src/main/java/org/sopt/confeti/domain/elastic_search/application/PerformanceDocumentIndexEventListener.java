package org.sopt.confeti.domain.elastic_search.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.domain.elastic_search.PerformanceDocument;
import org.sopt.confeti.global.common.ExecutorName;
import org.sopt.confeti.global.event.PerformanceDocumentIndexEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PerformanceDocumentIndexEventListener {

    private final PerformanceSearchService performanceSearchService;

    @Async(ExecutorName.PERFORMANCE_EXECUTOR)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePerformanceDocumentIndex(PerformanceDocumentIndexEvent event) {
        try {
            PerformanceDocument document = PerformanceDocument.create(event.performanceDTO());
            performanceSearchService.save(List.of(document));
        } catch (Exception e) {
            log.error(
                "PerformanceDocumentIndexEventListener.handlePerformanceDocumentIndex : ES 인덱싱 실패. performanceId : {}",
                event.performanceDTO().id(), e
            );
        }
    }
}
