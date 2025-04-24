package org.sopt.confeti.domain.elastic_search.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.elastic_search.PerformanceDocument;
import org.sopt.confeti.global.common.constant.PerformanceType;
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
    private static final String TYPE = "type";

    private final ElasticsearchOperations elasticsearchOperations;

    public List<PerformanceDocument> searchByTitleAndTypePartialMatch(String term, PerformanceType type) {
        Query partialMatchedQuery = NativeQuery.builder()
                .withQuery(query -> query
                        .bool(bool -> {
                            bool.must(must -> must
                                    .match(match -> match
                                            .field(TITLE_PARTIAL)
                                            .query(term)
                                    )
                            );

                            if (type != PerformanceType.PERFORMANCE) {
                                bool.must(must -> must
                                        .match(match -> match
                                                .field(TYPE)
                                                .query(type.getType())
                                        )
                                );
                            }

                            return bool;
                        })
                )
                .build();

        SearchHits<PerformanceDocument> hits = elasticsearchOperations.search(partialMatchedQuery,
                PerformanceDocument.class);

        return hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();
    }
}
