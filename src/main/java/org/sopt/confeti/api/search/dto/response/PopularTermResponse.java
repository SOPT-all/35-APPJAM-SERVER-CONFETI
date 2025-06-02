package org.sopt.confeti.api.search.dto.response;

import org.sopt.confeti.api.search.facade.dto.response.PopularTermDTO;

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
