package org.sopt.confeti.domain.elastic_search.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.elastic_search.PerformanceDocument;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PerformanceSearchOperator {

    private static final String TITLE_PARTIAL = "title.partial";

    private final ElasticsearchOperations elasticsearchOperations;

    public List<PerformanceDocument> searchByTitlePartialMatch(String term) {
        Query partialMatchedQuery = NativeQuery.builder()
                .withQuery(query -> query
                        .match(match -> match
                                .field(TITLE_PARTIAL)
                                .query(term)
                        )
                )
                .build();

        SearchHits<PerformanceDocument> hits = elasticsearchOperations.search(partialMatchedQuery,
                PerformanceDocument.class);

        return hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();
    }
}
