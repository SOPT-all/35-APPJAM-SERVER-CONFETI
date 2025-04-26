package org.sopt.confeti.domain.elastic_search.infra;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.elastic_search.SearchTermDocument;
import org.sopt.confeti.domain.elastic_search.application.dto.response.PopularTermResult;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchTermOperator {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final long POPULAR_SEARCH_TERMS_DATE_BEFORE = 30L;
    private static final String FIELD_TIMESTAMP = "timestamp";
    private static final String FIELD_SEARCH_TERM = "searchTerm";
    private static final String AGGREGATION_NAME = "popular_search_terms";

    private final ElasticsearchOperations elasticsearchOperations;

    public List<PopularTermResult> getPopularSearchTerms(int limit) {
        LocalDateTime endAt = LocalDateTime.now();
        LocalDateTime startAt = endAt.minusDays(POPULAR_SEARCH_TERMS_DATE_BEFORE);

        Query query = NativeQuery.builder()
                .withQuery(q -> q
                        .range(r -> r
                                .date(d -> d
                                        .field(FIELD_TIMESTAMP)
                                        .gte(startAt.format(dateTimeFormatter))
                                        .lte(endAt.format(dateTimeFormatter))
                                )
                        )
                )
                .withAggregation(AGGREGATION_NAME, Aggregation.of(a -> a
                                .terms(t -> t
                                        .field(FIELD_SEARCH_TERM)
                                        .size(limit)
                                )
                        )
                )
                .build();

        SearchHits<SearchTermDocument> searchHits = elasticsearchOperations.search(query, SearchTermDocument.class);

        // Aggregation 결과 추출 (공식 API)
        ElasticsearchAggregations aggregations = (ElasticsearchAggregations) searchHits.getAggregations();
        if (Objects.isNull(aggregations)) {
            return List.of();
        }

        ElasticsearchAggregation agg = aggregations.get(AGGREGATION_NAME);
        if (Objects.isNull(agg)) {
            return List.of();
        }

        StringTermsAggregate termsAgg = agg.aggregation().getAggregate().sterms();
        if (Objects.isNull(termsAgg)) {
            return List.of();
        }

        List<StringTermsBucket> buckets = termsAgg.buckets().array();

        // 순위 매기면서 결과 변환 (예시: SearchTermResult에 rank, keyword, count 포함)
        return IntStream.range(0, buckets.size())
                .mapToObj(i -> {
                    StringTermsBucket bucket = buckets.get(i);
                    return PopularTermResult.of(i + 1, bucket.key().stringValue());
                })
                .toList();
    }
}
