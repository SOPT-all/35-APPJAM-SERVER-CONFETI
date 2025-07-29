package org.sopt.confeti.domain.elastic_search.infra;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.elastic_search.PerformanceDocument;
import org.sopt.confeti.global.common.constant.PerformanceStatus;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;
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
    private static final String END_AT = "type";
    private static final String CURRENT_DATE = LocalDate.now().toString();

    private final ElasticsearchOperations elasticsearchOperations;

    private static class BoolQueryBuilder {

        private final BoolQuery.Builder boolQueryBuilder;
        private boolean isNotPerformanceType;
        private boolean isUpcomingStatus;

        private BoolQueryBuilder() {
            this.boolQueryBuilder = QueryBuilders.bool();
            isNotPerformanceType = isUpcomingStatus = true;
        }

        public static BoolQueryBuilder builder() {
            return new BoolQueryBuilder();
        }

        public BoolQueryBuilder ifNotPerformanceType(PerformanceType_DEPRECATED type) {
            if (type == PerformanceType_DEPRECATED.PERFORMANCE) {
                isNotPerformanceType = false;
            }

            return this;
        }

        public BoolQueryBuilder ifUpcomingStatus(PerformanceStatus status) {
            if (status != PerformanceStatus.UPCOMING) {
                isUpcomingStatus = false;
            }

            return this;
        }

        public BoolQueryBuilder match(String field, String value) {
            boolQueryBuilder.must(must -> must
                    .match(match -> match
                            .field(field)
                            .query(value)
                    )
            );

            return this;
        }

        public BoolQueryBuilder matchType(PerformanceType_DEPRECATED type) {
            if (isNotPerformanceType) {
                boolQueryBuilder.must(must -> must
                        .match(match -> match
                                .field(TYPE)
                                .query(type.getType())
                        )
                );
            }

            return this;
        }

        public BoolQueryBuilder rangeDateGte(String field, String value) {
            if (isUpcomingStatus) {
                boolQueryBuilder.must(must -> must
                        .range(range -> range
                                .date(date -> date
                                        .field(field)
                                        .gte(value)
                                )
                        )
                );

            }

            return this;
        }

        public BoolQuery build() {
            return this.boolQueryBuilder.build();
        }
    }

    public List<PerformanceDocument> searchByTitleAndTypePartialMatch(String term, PerformanceType_DEPRECATED type,
                                                                      PerformanceStatus status) {
        Query partialMatchedQuery = NativeQuery.builder()
                .withQuery(query -> query.bool(
                                BoolQueryBuilder.builder()
                                        .match(TITLE_PARTIAL, term)
                                        .ifNotPerformanceType(type)
                                        .matchType(type)
                                        .ifUpcomingStatus(status)
                                        .rangeDateGte(END_AT, CURRENT_DATE)
                                        .build()
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
