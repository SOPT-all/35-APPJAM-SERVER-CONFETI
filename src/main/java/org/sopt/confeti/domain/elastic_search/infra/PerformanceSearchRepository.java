package org.sopt.confeti.domain.elastic_search.infra;

import java.util.List;
import org.sopt.confeti.domain.elastic_search.PerformanceDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PerformanceSearchRepository extends ElasticsearchRepository<PerformanceDocument, Long> {

    @Query("{\"bool\": {\"should\": [{\"prefix\": {\"title.keyword\": \"?0\"}}, {\"match_phrase_prefix\": {\"title\": \"?0\"}}]}}")
    List<PerformanceDocument> findPerformanceDocumentsByTitleStartingWith(String title);
}
