package org.sopt.confeti.domain.elastic_search.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchTermOperator {

    private final ElasticsearchOperations elasticsearchOperations;
}
