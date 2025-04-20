package org.sopt.confeti.domain.elastic_search.infra;

import org.sopt.confeti.domain.elastic_search.PerformanceDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PerformanceSearchRepository extends ElasticsearchRepository<PerformanceDocument, Long> {
}
