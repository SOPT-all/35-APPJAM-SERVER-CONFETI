package org.sopt.confeti.api.search_term.dto.response;

import org.sopt.confeti.api.search_term.facade.dto.response.PopularTermDTO;

public record PopularTermResponse(
        int rank,
        String popularTerm
) {
    public static PopularTermResponse from(PopularTermDTO popularTermDTO) {
        return new PopularTermResponse(
                popularTermDTO.rank(),
                popularTermDTO.popularTerm()
        );
    }
}
