package org.sopt.confeti.domain.elastic_search.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.elastic_search.SearchTermDocument;
import org.sopt.confeti.domain.elastic_search.infra.SearchTermOperator;
import org.sopt.confeti.domain.elastic_search.infra.SearchTermRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchTermService {

    private final SearchTermOperator searchTermOperator;
    private final SearchTermRepository searchTermRepository;

    public void write(String searchTerm) {
        searchTermRepository.save(SearchTermDocument.create(searchTerm));
    }
}
