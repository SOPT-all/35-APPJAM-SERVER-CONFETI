package org.sopt.confeti.api.search_term.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.elastic_search.application.dto.response.PopularTermResult;

public record PopularTermsDTO(
        List<PopularTermDTO> popularTerms
) {
    public static PopularTermsDTO from(List<PopularTermResult> searchResults) {
        return new PopularTermsDTO(
                searchResults.stream()
                        .map(PopularTermDTO::from)
                        .toList()
        );
    }
}
