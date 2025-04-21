package org.sopt.confeti.domain.elastic_search.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.elastic_search.PerformanceDocument;
import org.sopt.confeti.domain.elastic_search.application.dto.response.SearchPerformanceResult;
import org.sopt.confeti.domain.elastic_search.infra.PerformanceSearchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PerformanceSearchService {

    private final PerformanceSearchRepository performanceSearchRepository;

    @Transactional
    public void deleteAll() {
        performanceSearchRepository.deleteAll();
    }

    @Transactional
    public void save(List<PerformanceDocument> performanceDocuments) {
        performanceSearchRepository.saveAll(performanceDocuments);
    }

    @Transactional(readOnly = true)
    public List<SearchPerformanceResult> getPerformancesByTitle(String title, int limit) {
        List<PerformanceDocument> performances = performanceSearchRepository.findPerformanceDocumentsByTitleStartsWith(
                title);

        return performances.stream()
                .map(SearchPerformanceResult::from)
                .limit(limit)
                .toList();
    }
}
