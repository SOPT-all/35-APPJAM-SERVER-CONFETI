package org.sopt.confeti.api.search_term.dto.response;

import java.util.List;
import org.sopt.confeti.api.search_term.facade.dto.response.PopularTermsDTO;

public record PopularTermsResponse(
        List<PopularTermResponse> popularTerms
) {
    public static PopularTermsResponse from(PopularTermsDTO popularTermsDTO) {
        return new PopularTermsResponse(
                popularTermsDTO.popularTerms().stream()
                        .map(PopularTermResponse::from)
                        .toList()
        );
    }
}
