package org.sopt.confeti.api.search_term.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.search_term.facade.dto.response.PopularTermsDTO;
import org.sopt.confeti.domain.elastic_search.application.SearchTermService;
import org.sopt.confeti.global.annotation.Facade;

@Facade
@RequiredArgsConstructor
public class SearchTermFacade {

    private final SearchTermService searchTermService;

    public PopularTermsDTO getPopularSearchTerms(int limit) {
        return PopularTermsDTO.from(searchTermService.getPopularSearchTerms(limit));
    }
}
