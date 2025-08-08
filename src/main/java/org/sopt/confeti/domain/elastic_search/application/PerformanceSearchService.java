package org.sopt.confeti.domain.elastic_search.application;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.elastic_search.PerformanceDocument;
import org.sopt.confeti.domain.elastic_search.application.dto.response.SearchPerformanceResult;
import org.sopt.confeti.domain.elastic_search.infra.PerformanceSearchOperator;
import org.sopt.confeti.domain.elastic_search.infra.PerformanceSearchRepository;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.global.common.constant.PerformanceStatus;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformanceSearchService {

    private final PerformanceSearchRepository performanceSearchRepository;
    private final PerformanceSearchOperator performanceSearchOperator;

    public void deleteAll() {
        performanceSearchRepository.deleteAll();
    }

    public void save(List<PerformanceDocument> performanceDocuments) {
        performanceSearchRepository.saveAll(performanceDocuments);
    }

    public List<SearchPerformanceResult> getPerformancesByTitleAndTypePartialMatched(String ptitle,
                                                                                     PerformanceType ptype) {
        List<PerformanceDocument> performances = performanceSearchOperator.searchByTitleAndTypePartialMatch(
                clearSentence(ptitle), ptype, PerformanceStatus.ALL);

        return performances.stream()
                .map(SearchPerformanceResult::from)
                .toList();
    }

    public List<SearchPerformanceResult> getExpectedPerformancesByTitleAndTypePartialMatched(String ptitle,
                                                                                             PerformanceType ptype) {
        List<PerformanceDocument> performances = performanceSearchOperator.searchByTitleAndTypePartialMatch(
                clearSentence(ptitle), ptype, PerformanceStatus.ALL);

        return performances.stream()
                .map(SearchPerformanceResult::from)
                .filter(performance -> !performance.endAt().isBefore(LocalDate.now()))
                .toList();
    }

    public List<SearchPerformanceResult> getPerformancesByTitle(String title, int limit, PerformanceStatus status) {
        List<PerformanceDocument> performances = performanceSearchOperator.searchByTitleAndTypePartialMatch(title,
                PerformanceType.PERFORMANCE, status);

        return performances.stream()
                .map(SearchPerformanceResult::from)
                .limit(limit)
                .toList();
    }

    private String clearSentence(String sentence) {
        return sentence.replaceAll("\"", "").replace("*", "\\*");
    }
}
