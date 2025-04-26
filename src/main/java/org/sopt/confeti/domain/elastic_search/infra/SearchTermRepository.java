package org.sopt.confeti.domain.elastic_search.infra;

import org.sopt.confeti.domain.elastic_search.SearchTermDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchTermRepository extends ElasticsearchRepository<SearchTermDocument, String> {
}
