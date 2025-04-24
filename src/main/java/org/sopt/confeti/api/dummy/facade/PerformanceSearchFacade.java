package org.sopt.confeti.api.dummy.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.elastic_search.PerformanceDocument;
import org.sopt.confeti.domain.elastic_search.application.PerformanceSearchService;
import org.sopt.confeti.domain.view.performance.application.PerformanceService;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.annotation.Facade;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class PerformanceSearchFacade {

    private final PerformanceSearchService performanceSearchService;
    private final PerformanceService performanceService;

    @Transactional
    public void batch() {
        performanceSearchService.deleteAll();
        List<PerformanceDTO> performances = performanceService.getAllPerformances();
        List<PerformanceDocument> performanceDocuments = performances.stream()
                .map(PerformanceDocument::create)
                .toList();

        performanceSearchService.save(performanceDocuments);
    }
}
