package org.sopt.confeti.api.search.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.search.facade.dto.response.PopularTermsDTO;
import org.sopt.confeti.domain.elastic_search.application.SearchTermService;
import org.sopt.confeti.global.annotation.Facade;

@Facade
@RequiredArgsConstructor
public class SearchFacade {

    private final SearchTermService searchTermService;

    public PopularTermsDTO getPopularSearchTerms(int limit) {
        return PopularTermsDTO.from(searchTermService.getPopularSearchTerms(limit));
    }
}
