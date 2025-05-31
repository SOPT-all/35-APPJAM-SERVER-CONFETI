package org.sopt.confeti.api.search.facade.dto.response;

import org.sopt.confeti.domain.elastic_search.application.dto.response.PopularTermResult;

public record PopularTermDTO(
        int rank,
        String popularTerm
) {
    public static PopularTermDTO from(PopularTermResult searchResult) {
        return new PopularTermDTO(
                searchResult.rank(),
                searchResult.popularTerm()
        );
    }
}
